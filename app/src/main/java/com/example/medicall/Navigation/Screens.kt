package com.example.medicall.Navigation

sealed class Screens(val route: String) {
    object MainScreen : Screens("login")
    object DoctorHome : Screens("doctorhome/{userId}")
    object Add : Screens("addPrescription")
    object Register : Screens("register")
    object DoctorDetailScreen : Screens("doctorInfo")
    object Booking : Screens("booking")
    object Home : Screens("home/{userId}")
    object Appointments : Screens("appointments")
}
