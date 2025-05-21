package com.example.medicall.ui.Navigation

sealed class Screens(val route: String) {
    object Register : Screens("Register")
    object Home : Screens("Home")
    object Booking : Screens("Booking")
    object MainScreen : Screens("Login")

    object PatientQRCode : Screens("PatientQRCode/{appointmentId}") {
        fun createRoute(appointmentId: String) = "PatientQRCode/$appointmentId"
    }
}
