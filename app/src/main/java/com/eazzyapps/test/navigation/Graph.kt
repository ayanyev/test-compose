package com.eazzyapps.test.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import com.eazzyapps.repositories.domain.models.GitHubRepo
import com.eazzyapps.repositories.ui.composables.DetailsScreen
import com.eazzyapps.repositories.ui.composables.MainScreen
import com.eazzyapps.repositories.ui.navigation.RepositoryScreen

@Composable
fun AppNavHost(controller: NavHostController) {
    NavHost(navController = controller, startDestination = RepositoryScreen.RepoListScreen.route) {
        composable(
            route = RepositoryScreen.RepoListScreen.route,
            arguments = listOf(
                navArgument("label") {
                    type = NavType.StringType
                    defaultValue = "Repositories"
                }
            ),
            content = { MainScreen() }
        )
        composable(
            route = RepositoryScreen.RepoDetailsScreen.route,
            arguments = listOf(
                navArgument("label") {
                    type = NavType.StringType
                    defaultValue = "Details"
                },
                navArgument("repoId") {
                    type = NavType.IntType
                }
            ),
            content = { backStackEntry ->
                val repoId = checkNotNull(
                    backStackEntry.arguments?.getInt("repoId")
                )
                DetailsScreen(repoId)
            }
        )
    }
}