package com.example.medicall.ui.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.medicall.ui.preferences.readId
import com.example.medicall.ui.screens.*

@Composable
fun Navigation(navController: NavHostController) {
    val context = LocalContext.current
    val userInfo = readId(context)

    val startDestination = if (userInfo.email != null && userInfo.pwd != null) {
        Screens.Home.route
    } else {
        Screens.Home.route
    }
    NavHost(navController = navController, startDestination = startDestination){
        composable(route = Screens.Register.route) {
            Register(navController = navController)
        }
        /*composable(route = Screens.Home.route) {
            Home(navController = navController)
        }*/
        composable(route = Screens.MainScreen.route) {
            Login(navController = navController)
        }
       /* composable(route = Screens.DoctorInfo.route) {
            DoctorInfo(navController = navController)
        }*/
        composable(route = Screens.Booking.route) {
            Booking()
        }
    }
}