package com.eazzyapps.repositories.data.remote.retrofit.models


import com.eazzyapps.repositories.data.local.CommitInfoLocal
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommitInfoDto(
    val commit: Commit,
    val sha: String
)

data class Commit(
    val author: Author,
    val message: String,
)

data class Author(
    val date: String,
    val email: String,
    val name: String
)

fun CommitInfoDto.toLocal(repoId: Int) = CommitInfoLocal(
    sha = sha,
    repoId = repoId.toLong(),
    date = commit.author.date,
    name = commit.author.name,
    message = commit.message,
    email = commit.author.email
)

fun List<CommitInfoDto>.toLocal(repoId: Int) = map { it.toLocal(repoId) }