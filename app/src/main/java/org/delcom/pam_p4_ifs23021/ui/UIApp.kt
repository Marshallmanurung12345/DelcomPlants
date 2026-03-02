package org.delcom.pam_p4_ifs23021.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.delcom.pam_p4_ifs23021.helper.ConstHelper
import org.delcom.pam_p4_ifs23021.ui.screens.*
import org.delcom.pam_p4_ifs23021.ui.viewmodels.PlantViewModel
import org.delcom.pam_p4_ifs23021.ui.viewmodels.DestinationViewModel

@Composable
fun UIApp(
    plantViewModel: PlantViewModel,
    destinationViewModel: DestinationViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ConstHelper.RouteNames.Home.path
    ) {
        composable(ConstHelper.RouteNames.Home.path) {
            HomeScreen(navController = navController)
        }

        composable(ConstHelper.RouteNames.Profile.path) {
            ProfileScreen(navController = navController, plantViewModel = plantViewModel)
        }

        composable(ConstHelper.RouteNames.Plants.path) {
            PlantsScreen(navController = navController, plantViewModel = plantViewModel)
        }

        composable(ConstHelper.RouteNames.PlantsAdd.path) {
            PlantsAddScreen(navController = navController, plantViewModel = plantViewModel)
        }

        composable(
            route = ConstHelper.RouteNames.PlantsDetail.path,
            arguments = listOf(navArgument("plantId") { type = NavType.StringType })
        ) { backStackEntry ->
            val plantId = backStackEntry.arguments?.getString("plantId") ?: ""
            PlantsDetailScreen(
                navController = navController,
                plantViewModel = plantViewModel,
                plantId = plantId
            )
        }

        composable(
            route = ConstHelper.RouteNames.PlantsEdit.path,
            arguments = listOf(navArgument("plantId") { type = NavType.StringType })
        ) { backStackEntry ->
            val plantId = backStackEntry.arguments?.getString("plantId") ?: ""
            PlantsEditScreen(
                navController = navController,
                plantViewModel = plantViewModel,
                plantId = plantId
            )
        }

        // ==========================
        // ✅ WISATA SAMOSIR (BARU)
        // ==========================
        composable(ConstHelper.RouteNames.Destinations.path) {
            DestinationsScreen(
                navController = navController,
                destinationViewModel = destinationViewModel
            )
        }

        composable(ConstHelper.RouteNames.DestinationsAdd.path) {
            DestinationAddScreen(
                navController = navController,
                destinationViewModel = destinationViewModel
            )
        }

        composable(
            route = ConstHelper.RouteNames.DestinationsDetail.path,
            arguments = listOf(navArgument("destinationId") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("destinationId") ?: ""
            DestinationDetailScreen(
                navController = navController,
                destinationId = id,
                destinationViewModel = destinationViewModel
            )
        }

        composable(
            route = ConstHelper.RouteNames.DestinationsEdit.path,
            arguments = listOf(navArgument("destinationId") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("destinationId") ?: ""
            DestinationEditScreen(
                navController = navController,
                destinationId = id,
                destinationViewModel = destinationViewModel
            )
        }
    }
}