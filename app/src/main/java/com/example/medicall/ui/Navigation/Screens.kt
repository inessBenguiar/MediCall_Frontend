package com.example.medicall.ui.Navigation

sealed class Screens (val route: String) {
    object Register : Screens("Register")
    object Home : Screens("Home")
    //object DoctorInfo : Screens("DoctorInfo")
    object Booking : Screens("Booking")
    object MainScreen: Screens("Login")

}