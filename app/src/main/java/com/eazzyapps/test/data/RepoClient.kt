package com.eazzyapps.test.data

import com.eazzyapps.test.data.models.CommitInfoDto
import com.eazzyapps.test.data.models.GitHubRepoDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface RepoClient {

    @GET("users/{owner}/repos")
    suspend fun getPublicRepositories(
        @Path("owner") owner: String
    ): List<GitHubRepoDto>

    @GET
    suspend fun getRepositoryCommits(
        @Url url: String
    ): Response<List<CommitInfoDto>>
}