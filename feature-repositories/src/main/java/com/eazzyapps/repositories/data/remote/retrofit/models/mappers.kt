package com.eazzyapps.repositories.data.remote.retrofit.models

import com.eazzyapps.repositories.domain.models.CommitInfo
import com.eazzyapps.repositories.domain.models.GitHubRepo

fun CommitInfoDto.toDomain() = CommitInfo(
    sha = sha,
    date = commit.author.date,
    author = commit.author.name,
    message = commit.message
)

fun GitHubRepoDto.toDomain() = GitHubRepo(
    id = id,
    name = name,
    description = description,
    owner = owner.login,
    avatarUrl = owner.avatar_url,
    createdAt = createdAt,
    license = license?.name,
    forks = forks_count,
    stars = stargazers_count,
    watchers = watchers_count
)

@JvmName("repListMapper")
fun List<GitHubRepoDto>.toDomain() = map { it.toDomain() }

@JvmName("commitListMapper")
fun List<CommitInfoDto>.toDomain() = map { it.toDomain() }