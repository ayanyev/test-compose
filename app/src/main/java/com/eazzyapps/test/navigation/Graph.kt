package com.eazzyapps.test.navigation

import android.os.Bundle
import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import com.eazzyapps.test.domain.models.GitHubRepo
import com.eazzyapps.test.ui.composables.DetailsScreen
import com.eazzyapps.test.ui.composables.MainScreen

@Composable
fun AppNavHost(controller: NavHostController) {
    NavHost(navController = controller, startDestination = Screen.Main.route) {
        composable(
            route = Screen.Main.route,
            arguments = listOf(
                navArgument("label") {
                    type = NavType.StringType
                    defaultValue = "Repositories"
                }
            ),
            content = { MainScreen() }
        )
        composable(
            route = "repos/details",
            arguments = listOf(
                navArgument("label") {
                    type = NavType.StringType
                    defaultValue = "Details"
                }
            ),
            content = {
                val repo = checkNotNull(
                    controller.previousBackStackEntry?.arguments?.getParcelable<GitHubRepo>("repo")
                )
                DetailsScreen(repo)
            }
        )
    }
}

//TODO think on how to provide routes with arguments in more
// generic way without copypasting
sealed class Screen(
    val route: String,
    val popBackStack: Boolean = false,
    val args: Map<String, Parcelable> = mapOf()
) {
    object Previous : Screen("back")
    object Main : Screen("repos/list")
    class Details(repo: GitHubRepo) : Screen("repos/details", args = mapOf("repo" to repo))
}

/**
 * Set parcelable arguments to [NavController.getCurrentBackStackEntry]
 * and navigate to a route in the current NavGraph.
 * After navigation get these values back from [NavController.getPreviousBackStackEntry]
 * arguments
 *
 * @param parcelableArgs map of parcelable arguments with respective keys
 * @see navigate(string, NavOptionsBuilder.() -> Unit)
 */
fun NavController.navigate(
    route: String,
    parcelableArgs: Map<String, Parcelable> = mapOf(),
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    currentBackStackEntry?.arguments =
        Bundle().apply {
            for (arg in parcelableArgs) {
                putParcelable(arg.key, arg.value)
            }
        }
    navigate(route, builder)
}