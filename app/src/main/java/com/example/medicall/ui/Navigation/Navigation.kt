package com.example.medicall.ui.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.medicall.ui.preferences.readId
import com.example.medicall.ui.repository.RepositoryHolder
import com.example.medicall.ui.screens.Booking
import com.example.medicall.ui.screens.DoctorInfo
import com.example.medicall.ui.screens.Home
import com.example.medicall.ui.screens.Login
import com.example.medicall.ui.screens.Register
import com.example.medicall.ui.viewModel.AuthModel

@Composable
fun Navigation(navController: NavHostController) {
    val context = LocalContext.current
    val userInfo = readId(context)
    val authModel = AuthModel(RepositoryHolder.authRepository)

    val startDestination = if (userInfo.email != null && userInfo.pwd != null) {
        Screens.Home.route
    } else {
        Screens.MainScreen.route
    }
    NavHost(navController = navController, startDestination = startDestination){
        composable(route = Screens.Register.route) {
            Register(navController = navController)
        }
        composable(route = Screens.Home.route) {
            Home(navController = navController)
        }
        composable(route = Screens.MainScreen.route) {
            Login(authModel,navController = navController)
        }
        composable(route = Screens.DoctorInfo.route) {
            DoctorInfo(navController = navController)
        }
        composable(route = Screens.Booking.route) {
            Booking(navController = navController)
        }
    }
}