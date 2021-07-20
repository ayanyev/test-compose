package com.eazzyapps.test.data

import com.eazzyapps.test.data.models.toDomain
import com.eazzyapps.test.domain.Repository
import com.eazzyapps.test.domain.models.CommitInfo
import com.eazzyapps.test.domain.models.GitHubRepo
import io.reactivex.rxjava3.core.Observable

class RepositoryImpl(private val client: RepoClient) : Repository {

    // no pagination implemented
    override fun getPublicRepositories(owner: String): Observable<List<GitHubRepo>> {
        return client.getPublicRepositories(owner).map { it.toDomain() }
    }

    // get all commits at once
    // for pagination used next page link from response headers
    override fun getRepositoryCommits(owner: String, repoName: String): Observable<List<CommitInfo>> {
        return Observable.create<List<CommitInfo>> { emitter ->
            var nextLink: String? =
                "https://api.github.com/repos/$owner/$repoName/commits?per_page=100&page=1"
            while (nextLink != null) {
                val response = client.getRepositoryCommits(nextLink).execute()
                if (response.isSuccessful) {
                    nextLink = response.headers()["link"]?.parseLinks()?.get("next")
                    emitter.onNext(response.body()?.toDomain())
                } else emitter.onError(Exception("Error with code ${response.code()} received"))
            }
            emitter.onComplete()
        }.scan { t1, t2 -> t1.plus(t2) }.takeLast(1)
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