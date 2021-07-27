package com.eazzyapps.repositories.data

import com.eazzyapps.repositories.data.remote.retrofit.models.toDomain
import com.eazzyapps.repositories.data.remote.retrofit.RepoClient
import com.eazzyapps.repositories.domain.Repository
import com.eazzyapps.repositories.domain.models.CommitInfo
import com.eazzyapps.repositories.domain.models.GitHubRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class RepositoryImpl(
    private val client: RepoClient,
    private val dispatcher: CoroutineDispatcher
) : Repository {

    // no pagination implemented
    override suspend fun getPublicRepositories(owner: String): List<GitHubRepo> =
        withContext(dispatcher) {
            client.getPublicRepositories(owner).toDomain()
        }

    // get all commits at once
    // for pagination used next page link from response headers
    override suspend fun getRepositoryCommits(owner: String, repoName: String): List<CommitInfo> =
        withContext(dispatcher) {
            val result = mutableListOf<CommitInfo>()
            var nextLink: String? =
                "https://api.github.com/repos/$owner/$repoName/commits?per_page=100&page=1"
            while (nextLink != null) {
                val response = client.getRepositoryCommits(nextLink)
                if (response.isSuccessful) {
                    nextLink = response.headers()["link"]?.parseLinks()?.get("next")
                    result.addAll(response.body()?.toDomain() ?: listOf())
                } else throw Exception("Error with code ${response.code()} received")
            }
            result
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