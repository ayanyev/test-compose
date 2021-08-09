package com.eazzyapps.repositories.data

import androidx.paging.*
import com.eazzyapps.repositories.Database
import com.eazzyapps.repositories.data.local.GitHubRepoLocal
import com.eazzyapps.repositories.data.local.RemoteKeys
import com.eazzyapps.repositories.data.local.models.toDomain
import com.eazzyapps.repositories.data.remote.retrofit.RepoClient
import com.eazzyapps.repositories.data.remote.retrofit.models.CommitInfoDto
import com.eazzyapps.repositories.data.remote.retrofit.models.toLocal
import com.eazzyapps.repositories.domain.Repository
import com.eazzyapps.repositories.domain.models.CommitInfo
import com.eazzyapps.repositories.domain.models.GitHubRepo
import com.squareup.sqldelight.android.paging3.QueryPagingSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class RepositoryImpl(
    private val db: Database,
    private val client: RepoClient,
    private val dispatcher: CoroutineDispatcher
) : Repository, RemoteMediator<Long, GitHubRepoLocal>() {

    private val repoQueries = db.githubRepoTableQueries

    private val commitQueries = db.commitInfoTableQueries

    private lateinit var repositoriesOwner: String

    override fun getPagedPublicRepositories(owner: String): Flow<PagingData<GitHubRepo>> {
        repositoriesOwner = owner
        return pager.flow.map { data -> data.map { it.toDomain() } }
    }

    override suspend fun getRepositoryById(id: Int): GitHubRepo =
        withContext(dispatcher) {
            repoQueries.selectById(id = id.toLong()).executeAsOne().toDomain()
        }

    override suspend fun getPublicRepositories(owner: String): List<GitHubRepo> {
        TODO("Not yet implemented")
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

    override suspend fun load(loadType: LoadType, state: PagingState<Long, GitHubRepoLocal>): MediatorResult {

        val page = when (loadType) {

            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }

        }

        val pageSize = state.config.pageSize

        return try {

            val remotes = client.getPublicRepositories(repositoriesOwner, pageSize, page.toInt())

            val isLastPage = remotes.size < pageSize

            db.transaction {
                val nextKey = if (isLastPage) null else  page + 1
                val prevKey = if (page == 1L) null else page - 1
                remotes.forEach { repoQueries.insert(it.toLocal(nextKey, prevKey)) }
            }

            MediatorResult.Success(endOfPaginationReached = isLastPage)

        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            MediatorResult.Error(exception)
        }
    }

    private fun getRemoteKeyForLastItem(state: PagingState<Long, GitHubRepoLocal>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { repo -> repoQueries.remoteKeys(repo.id).executeAsOne() }
    }

    private fun getRemoteKeyForFirstItem(state: PagingState<Long, GitHubRepoLocal>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repo -> repoQueries.remoteKeys(repo.id).executeAsOne() }
    }

    private fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Long, GitHubRepoLocal>): RemoteKeys? {
        return state.anchorPosition?.let { position -> state.closestItemToPosition(position)
            ?.let { repo -> repoQueries.remoteKeys(repo.id).executeAsOne() }
        }
    }

    private val pager = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false
        ),
        remoteMediator = this,
        pagingSourceFactory = { pagingSource }
    )

    private val pagingSource: PagingSource<Long, GitHubRepoLocal>
        get() = QueryPagingSource(
            countQuery = repoQueries.countRepositories(),
            transacter = repoQueries,
            queryProvider = repoQueries::select
        )

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