package com.example.medicall

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.medicall.Navigation.Screens
import com.example.medicall.repository.RepositoryHolder
import com.example.medicall.ui.components.AddPrescriptionForm
import com.example.medicall.ui.components.AppointmentDetails
import com.example.medicall.ui.preferences.readId
import com.example.medicall.ui.screens.Booking
import com.example.medicall.ui.screens.DoctorHome
import com.example.medicall.ui.screens.DoctorInfo
import com.example.medicall.ui.screens.Home
import com.example.medicall.ui.screens.Login
import com.example.medicall.ui.screens.Register
import com.example.medicall.ui.theme.MedicallTheme
import com.example.medicall.viewmodel.DoctorModel
import com.example.medicall.ui.screens.Appointments



class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MedicallTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AppNavigator()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigator() {
    val navController = rememberNavController()
    val doctorModel = DoctorModel(RepositoryHolder.DoctorRepository)
    val context = LocalContext.current
    val start:String
    val userId = readId(context)
    //Is user still connected ?
    if ( userId != null){
        start = "home/${userId}"
    }else{
        start = Screens.MainScreen.route
    }

    NavHost(navController = navController, startDestination = start) {
        composable(Screens.MainScreen.route) {
           Login(navController)
        }

        composable(
            route = "doctorhome/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: -1
            if (userId != -1) {
                DoctorHome(navController, userId)
            }
        }
        composable(Screens.Add.route) {
            AddPrescriptionForm(navController)
        }

        composable(
            route = "home/{userId}"
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            Home(navController, doctorModel, userId)
        }

        composable(Screens.Register.route) {
            Register(navController)
        }
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
       composable("appointmentDetails/{appointmentId}") { backStackEntry ->
            val appointmentId = backStackEntry.arguments?.getString("appointmentId")?.toIntOrNull() ?: 0
            AppointmentDetails(navController = navController, appointmentId = appointmentId)
        }
        composable(route = com.example.medicall.ui.Navigation.Screens.Booking.route) {
            Booking()
        }


        composable(
            route = "PatientQRCode/{appointmentId}",
            arguments = listOf(navArgument("appointmentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val appointmentId = backStackEntry.arguments?.getString("appointmentId") ?: ""
            com.example.medicall.ui.screens.PatientQRCodeScreen(appointmentId = appointmentId)
        }

        composable("appointments") {
            Appointments(navController = navController, patientId = 21)
        }



    }
}