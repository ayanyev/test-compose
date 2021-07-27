package com.eazzyapps.repositories.domain.models

data class CommitInfo(
    val sha: String,
    val date: String,
    val author: String,
    val message: String
)