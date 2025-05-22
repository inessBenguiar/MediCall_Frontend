package com.example.medicall.Navigation


import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.medicall.ui.screens.AddEditPrescriptionScreen
import com.example.medicall.ui.screens.PrescriptionDetailScreen
import com.example.medicall.ui.screens.PrescriptionListScreen
import com.example.medicall.viewmodel.PrescriptionViewModel

@Composable
fun AppNavigation(viewModel: PrescriptionViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screens.PrescriptionList.route
    ) {
        composable(Screens.PrescriptionList.route) {
            PrescriptionListScreen(
                viewModel = viewModel,
                onPrescriptionClick = { prescriptionId ->
                    navController.navigate("${Screens.PrescriptionDetail.route}/$prescriptionId")
                },
                onAddClick = {
                    navController.navigate(Screens.AddPrescription.route)
                }
            )
        }

        composable(Screens.AddPrescription.route) {
            AddEditPrescriptionScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
            )
        }

       composable(
            route = "${Screens.PrescriptionDetail.route}/{prescriptionId}",
            arguments = listOf(
                navArgument("prescriptionId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val prescriptionId = backStackEntry.arguments?.getLong("prescriptionId") ?: 0L


            val rememberedPrescriptionId = remember { prescriptionId }

            PrescriptionDetailScreen(
                viewModel = viewModel,
                prescriptionId = rememberedPrescriptionId,
                onBack = { navController.popBackStack() },
                onEdit = { id ->
                    navController.navigate("${Screens.EditPrescription.route}/$id") {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = "${Screens.EditPrescription.route}/{prescriptionId}",
            arguments = listOf(
                navArgument("prescriptionId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val prescriptionId = backStackEntry.arguments?.getLong("prescriptionId") ?: 0L

            AddEditPrescriptionScreen(
                viewModel = viewModel,
                prescriptionId = prescriptionId,
                onBack = { navController.popBackStack() },
            )
        }
    }
}

