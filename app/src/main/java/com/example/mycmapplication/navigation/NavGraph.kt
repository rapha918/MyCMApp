package com.example.mycmapplication.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mycmapplication.DieResult1Screen
import com.example.mycmapplication.DieResult2Screen
import com.example.mycmapplication.DieResult3Screen
import com.example.mycmapplication.DieResult4Screen
import com.example.mycmapplication.DieResult5Screen
import com.example.mycmapplication.DieResult6Screen
import com.example.mycmapplication.MainRollScreen

@Composable
fun AppNavigationHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavGraph(navController = navController, modifier = modifier)
}

fun navigateToDieResult(navController: NavHostController, value: Int) {
    val targetRoute = when (value) {
        1 -> Screens.DieResult1.createRoute(22)
        2 -> Screens.DieResult2.createRoute(value)
        3 -> Screens.DieResult3.createRoute(value)
        4 -> Screens.DieResult4.route
        5 -> Screens.DieResult5.createRoute(value)
        else -> Screens.DieResult6.createRoute(value)
    }
    navController.navigate(targetRoute)
}

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screens.MainRoll.route,
        modifier = modifier
    ) {
        composable(Screens.MainRoll.route) { backStackEntry ->
            val updatedDie = backStackEntry.savedStateHandle.get<Int>("updatedDie")
            MainRollScreen(
                initialDie = updatedDie,
                onNavigateToResult = { value -> navigateToDieResult(navController, value) }
            )
        }

        composable(
            route = Screens.DieResult1.route,
            arguments = listOf(navArgument("temp") { type = NavType.IntType; defaultValue = 22 })
        ) { backStackEntry ->
            val temp = backStackEntry.arguments?.getInt("temp") ?: 22
            DieResult1Screen(
                initialTemp = temp,
                onBack = { updatedVal ->
                    navController.previousBackStackEntry?.savedStateHandle?.set("updatedDie", updatedVal)
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screens.DieResult2.route,
            arguments = listOf(navArgument("dieValue") { type = NavType.IntType; defaultValue = 2 })
        ) { backStackEntry ->
            val dieValue = backStackEntry.arguments?.getInt("dieValue") ?: 2
            DieResult2Screen(
                initialValue = dieValue,
                onBack = { updatedVal ->
                    navController.previousBackStackEntry?.savedStateHandle?.set("updatedDie", updatedVal)
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screens.DieResult3.route,
            arguments = listOf(navArgument("dieValue") { type = NavType.IntType; defaultValue = 3 })
        ) { backStackEntry ->
            val dieValue = backStackEntry.arguments?.getInt("dieValue") ?: 3
            DieResult3Screen(
                initialValue = dieValue,
                onBack = { updatedVal ->
                    navController.previousBackStackEntry?.savedStateHandle?.set("updatedDie", updatedVal)
                    navController.popBackStack()
                }
            )
        }

        composable(Screens.DieResult4.route) {
            DieResult4Screen(
                onNavigateToScreen = { screenNum -> navigateToDieResult(navController, screenNum) },
                onBack = { updatedVal ->
                    navController.previousBackStackEntry?.savedStateHandle?.set("updatedDie", updatedVal)
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screens.DieResult5.route,
            arguments = listOf(navArgument("dieValue") { type = NavType.IntType; defaultValue = 5 })
        ) { backStackEntry ->
            val dieValue = backStackEntry.arguments?.getInt("dieValue") ?: 5
            DieResult5Screen(
                initialValue = dieValue,
                onBack = { updatedVal ->
                    navController.previousBackStackEntry?.savedStateHandle?.set("updatedDie", updatedVal)
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screens.DieResult6.route,
            arguments = listOf(navArgument("dieValue") { type = NavType.IntType; defaultValue = 6 })
        ) { backStackEntry ->
            val dieValue = backStackEntry.arguments?.getInt("dieValue") ?: 6
            DieResult6Screen(
                initialValue = dieValue,
                onNavigateToResult = { value -> navigateToDieResult(navController, value) },
                onBack = { updatedVal ->
                    navController.previousBackStackEntry?.savedStateHandle?.set("updatedDie", updatedVal)
                    navController.popBackStack()
                }
            )
        }
    }
}
