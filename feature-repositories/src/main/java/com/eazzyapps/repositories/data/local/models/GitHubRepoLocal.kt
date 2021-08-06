package com.eazzyapps.repositories.data.local.models

import com.eazzyapps.repositories.data.local.GitHubRepoLocal
import com.eazzyapps.repositories.domain.models.GitHubRepo

fun GitHubRepoLocal.toDomain() = GitHubRepo(
    id = id.toInt(),
    name = authorName,
    description = description,
    owner = ownerLogin,
    avatarUrl = authorAvatarUrl,
    createdAt = createdAt,
    license = license,
    forks = forksCount.toInt(),
    stars = stargazersCount.toInt(),
    watchers = watchersCount.toInt()
)

fun List<GitHubRepoLocal>.toDomain() = map { it.toDomain() }