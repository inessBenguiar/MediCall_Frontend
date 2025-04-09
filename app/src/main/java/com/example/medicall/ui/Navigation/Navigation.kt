package com.example.medicall.ui.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.medicall.ui.screens.*

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.MainScreen.route){
        composable(route = Screens.MainScreen.route) {
            Register(navController = navController)
        }
        composable(route = Screens.Home.route) {
            Home(navController = navController)
        }
        composable(route = Screens.Login.route) {
            Login(navController = navController)
        }
        composable(route = Screens.DoctorInfo.route) {
            DoctorInfo(navController = navController)
        }
        composable(route = Screens.Booking.route) {
            Booking(navController = navController)
        }
    }
}