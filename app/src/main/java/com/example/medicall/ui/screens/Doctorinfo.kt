package com.example.medicall.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.medicall.ui.components.DoctorDetails

@Composable
fun DoctorInfo(navController: NavController) {
    val doctorId = navController.currentBackStackEntry?.arguments?.getString("id")
    val firstName = navController.currentBackStackEntry?.arguments?.getString("firstName")
    val familyName = navController.currentBackStackEntry?.arguments?.getString("familyName")
    val photoUrl = navController.currentBackStackEntry?.arguments?.getString("photoUrl")
    val address = navController.currentBackStackEntry?.arguments?.getString("address")
    val phone = navController.currentBackStackEntry?.arguments?.getString("phone")

    DoctorDetails(
        navController = navController,
        doctorId = doctorId!!.toInt() ,
        firstName = firstName,
        familyName = familyName,
        photoUrl = photoUrl,
        address = address,
        phone = phone
    )
}