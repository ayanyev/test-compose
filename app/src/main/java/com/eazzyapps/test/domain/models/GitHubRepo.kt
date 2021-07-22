package com.eazzyapps.test.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GitHubRepo(
    val id: Int,
    val name: String,
    val description: String?,
    val owner: String,
    val avatarUrl: String,
    val createdAt: String?,
    val license: String?,
    val forks: Int,
    val stars: Int,
    val watchers: Int
) : Parcelable