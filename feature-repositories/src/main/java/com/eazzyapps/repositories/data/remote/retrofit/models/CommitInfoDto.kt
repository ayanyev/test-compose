package com.eazzyapps.repositories.data.remote.retrofit.models


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