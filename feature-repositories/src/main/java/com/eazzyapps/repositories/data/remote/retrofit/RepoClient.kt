package com.eazzyapps.repositories.data.remote.retrofit

import com.eazzyapps.repositories.data.remote.retrofit.models.CommitInfoDto
import com.eazzyapps.repositories.data.remote.retrofit.models.GitHubRepoDto
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