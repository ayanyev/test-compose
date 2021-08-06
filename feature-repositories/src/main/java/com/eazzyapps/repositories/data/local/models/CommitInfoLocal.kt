package com.eazzyapps.repositories.data.local.models

import com.eazzyapps.repositories.data.local.CommitInfoLocal
import com.eazzyapps.repositories.domain.models.CommitInfo

fun CommitInfoLocal.toDomain() = CommitInfo(
    sha = sha,
    date = date,
    author = name,
    message = message
)

fun List<CommitInfoLocal>.toDomain() = map { it.toDomain() }