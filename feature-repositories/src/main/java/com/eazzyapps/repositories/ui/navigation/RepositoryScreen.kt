package com.eazzyapps.repositories.ui.navigation

import com.eazzyapps.test.common.Screen

sealed class RepositoryScreen(

    override val route: String,
    override val popBackStack: Boolean

) : Screen() {

    object RepoListScreen : RepositoryScreen("repos/list", false)
    object RepoDetailsScreen : RepositoryScreen("repos/{repoId}/details", false)

}