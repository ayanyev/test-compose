package com.eazzyapps.repositories.data.remote.retrofit.models

import com.eazzyapps.repositories.data.local.GitHubRepoLocal

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

fun GitHubRepoDto.toLocal(next: Long?, prev: Long?) = GitHubRepoLocal(
    id = id.toLong(),
    authorName = name,
    description = description,
    ownerLogin = owner.login,
    authorAvatarUrl = owner.avatar_url,
    createdAt = createdAt,
    license = license?.name,
    forksCount = forks_count.toLong(),
    stargazersCount = stargazers_count.toLong(),
    watchersCount = watchers_count.toLong(),
    nextKey = next,
    prevKey = prev

)

fun List<GitHubRepoDto>.toLocal(next: Long?, prev: Long?) = map { it.toLocal(next, prev) }