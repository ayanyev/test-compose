package com.eazzyapps.repositories.data.remote.retrofit.models

data class GitHubRepoDto(
    val id: Int,
    val name: String,
    val description: String?,
    val owner: Owner,
    val createdAt: String?,
    val license: License?,
    val forks_count: Int,
    val stargazers_count: Int,
    val watchers_count: Int
)

data class Owner(
    val login: String,
    val avatar_url: String
)

data class License(
    val name: String
)