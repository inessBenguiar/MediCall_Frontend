package com.example.medicall.ui.Navigation

sealed class Screens(val route: String) {
    object Register : Screens("Register")
    object Home : Screens("Home")
    object DoctorDetailScreen : Screens("DoctorInfo")
    object Booking : Screens("Booking")
    object MainScreen: Screens("Login")
    object Add : Screens("Add")
    object AddEditPrescription : Screens("addEditPrescription")
}