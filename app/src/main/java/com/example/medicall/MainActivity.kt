package com.example.medicall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.medicall.Navigation.Screens
import com.example.medicall.repository.AppointmentRepositoryImpl
import com.example.medicall.service.Endpoint
import com.example.medicall.ui.components.BookScreen
import com.example.medicall.ui.components.UserInformationScreen
import com.example.medicall.ui.screens.Booking
import com.example.medicall.ui.screens.DoctorInfo
import com.example.medicall.ui.screens.Home
import com.example.medicall.ui.screens.Login
import com.example.medicall.ui.screens.Register
import com.example.medicall.ui.theme.MedicallTheme
import com.example.medicall.viewmodel.AppointmentViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MedicallTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    //Booking()
                    UserInformationScreen()
                }
            }
        }
    }
}

@Composable
fun AppNavigator() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screens.MainScreen.route) {
        composable(Screens.MainScreen.route) {
            Login(navController)
        }
        composable(Screens.Home.route) {
            Home(navController) // Make sure to pass navController to Home
        }
        composable(Screens.Register.route) {
            Register(navController)
        }
        // Properly handle route parameters for doctor details
        composable(
            route = "${Screens.DoctorDetailScreen.route}?firstName={firstName}&familyName={familyName}&photoUrl={photoUrl}&address={address}&phone={phone}",
            arguments = listOf(
                navArgument("firstName") { type = NavType.StringType; nullable = true },
                navArgument("familyName") { type = NavType.StringType; nullable = true },
                navArgument("photoUrl") { type = NavType.StringType; nullable = true },
                navArgument("address") { type = NavType.StringType; nullable = true },
                navArgument("phone") { type = NavType.StringType; nullable = true }
            )
        ) {
            DoctorInfo(navController)
        }
    }
}