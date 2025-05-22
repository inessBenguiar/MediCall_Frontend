package com.example.medicall.Navigation

sealed class Screens(val route: String) {
    object Register : Screens("signup")
    object Home : Screens("home")
    object DoctorDetailScreen : Screens("doctor_detail")
    object MainScreen : Screens("login")
    object Add: Screens("prescription")
    object DoctorHome: Screens("doctorhome")
    object PrescriptionList : Screens("prescription_list")
    object AddPrescription : Screens("add_prescription")
    object PrescriptionDetail : Screens("prescription_detail")
    object EditPrescription : Screens("edit_prescription")
}
