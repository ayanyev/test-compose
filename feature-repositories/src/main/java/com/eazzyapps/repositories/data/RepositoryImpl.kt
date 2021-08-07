package com.eazzyapps.repositories.data

import com.eazzyapps.repositories.Database
import com.eazzyapps.repositories.data.local.models.toDomain
import com.eazzyapps.repositories.data.remote.retrofit.RepoClient
import com.eazzyapps.repositories.data.remote.retrofit.models.CommitInfoDto
import com.eazzyapps.repositories.data.remote.retrofit.models.toLocal
import com.eazzyapps.repositories.domain.Repository
import com.eazzyapps.repositories.domain.models.CommitInfo
import com.eazzyapps.repositories.domain.models.GitHubRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class RepositoryImpl(
    private val db: Database,
    private val client: RepoClient,
    private val dispatcher: CoroutineDispatcher
) : Repository {

    private val repoQueries = db.githubRepoTableQueries

    private val commitQueries = db.commitInfoTableQueries

    override suspend fun getRepositoryById(id: Int): GitHubRepo =
        withContext(dispatcher) {
            repoQueries.selectById(id = id.toLong()).executeAsOne().toDomain()
        }

    override suspend fun getPublicRepositories(owner: String): List<GitHubRepo> =
        withContext(dispatcher) {
            var repos = repoQueries.selectAll().executeAsList()
            if (repos.isEmpty()) {
                fetchRemoteRepositories(owner)
                repos = repoQueries.selectAll().executeAsList()
            }
            repos.toDomain()
        }

    private suspend fun fetchRemoteRepositories(owner: String) {
        client.getPublicRepositories(owner).also { remotes ->
            db.transaction {
                remotes.forEach { repoQueries.insert(it.toLocal()) }
            }
        }
    }

    // get all commits at once
    // for pagination used next page link from response headers
    override suspend fun getRepositoryCommits(repo: GitHubRepo): List<CommitInfo> =
        withContext(dispatcher) {
            var commits = commitQueries.selectByRepoId(repo.id.toLong()).executeAsList()
            if (commits.isEmpty()) {
                fetchRemoteRepositoryCommits(repo)
                commits = commitQueries.selectByRepoId(repo.id.toLong()).executeAsList()
            }
            commits.toDomain()
        }


    private suspend fun fetchRemoteRepositoryCommits(repo: GitHubRepo) {
        val commits = mutableListOf<CommitInfoDto>()
        var nextLink: String? =
            "https://api.github.com/repos/${repo.owner}/${repo.name}/commits?per_page=100&page=1"
        while (nextLink != null) {
            val response = client.getRepositoryCommits(nextLink)
            if (response.isSuccessful) {
                nextLink = response.headers()["link"]?.parseLinks()?.get("next")
                commits.addAll(response.body() ?: listOf())
            } else throw Exception("Error with code ${response.code()} received")
        }
        db.transaction {
            commits.forEach { commitQueries.insert(it.toLocal(repo.id)) }
        }
    }

    // link header in response
    // <https://api.github.com/repositories/10791045/commits?per_page=10&page=9>; rel="next",
    // <https://api.github.com/repositories/10791045/commits?per_page=10&page=9>; rel="last",
    // <https://api.github.com/repositories/10791045/commits?per_page=10&page=1>; rel="first",
    // <https://api.github.com/repositories/10791045/commits?per_page=10&page=7>; rel="prev"

    private fun String.parseLinks(): Map<String?, String?> =
        split(",").associateBy(
            keySelector = { Regex("(?<=rel=\")\\D{4}").find(it)?.value },
            valueTransform = { Regex("[^<]+(?=>;)").find(it)?.value }
        )

}