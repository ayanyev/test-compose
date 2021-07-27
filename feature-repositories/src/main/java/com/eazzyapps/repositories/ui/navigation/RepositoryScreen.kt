package com.eazzyapps.repositories.ui.navigation

import android.os.Parcelable
import com.eazzyapps.repositories.domain.models.GitHubRepo
import com.eazzyapps.test.common.Screen

sealed class RepositoryScreen(
    override val route: String,
    override val popBackStack: Boolean = false,
    override val parcelableArgs: Map<String, Parcelable> = mapOf()
) : Screen {
    object RepoList : RepositoryScreen("repos/list")
    class RepoDetails(repo: GitHubRepo) : RepositoryScreen("repos/details", parcelableArgs = mapOf("repo" to repo))
}