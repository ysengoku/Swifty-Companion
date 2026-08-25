package dev.ysengoku.swiftycompanion.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.ysengoku.swiftycompanion.ui.search.SearchScreen
import dev.ysengoku.swiftycompanion.ui.detail.DetailScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "search") {
        composable("search") {
            SearchScreen(onSubmit = { login ->
                navController.navigate("detail/$login")
            })
        }
        composable("detail/{login}") { backStackEntry ->
            val login = backStackEntry.arguments?.getString("login") ?: ""
            DetailScreen(
                login = login,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
