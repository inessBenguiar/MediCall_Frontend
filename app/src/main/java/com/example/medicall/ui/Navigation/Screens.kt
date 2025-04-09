package com.example.medicall.ui.Navigation

sealed class Screens (val route: String) {
    object MainScreen : Screens("Register")
    object Home : Screens("Home")
    object DoctorInfo : Screens("DoctorInfo")
    object Booking : Screens("Booking")
    object Login : Screens("Login")

}