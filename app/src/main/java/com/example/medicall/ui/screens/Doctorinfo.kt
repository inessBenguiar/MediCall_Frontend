package com.example.medicall.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.medicall.ui.components.DoctorDetails

@Composable
fun DoctorInfo(navController: NavController) {
    val doctorId = navController.currentBackStackEntry?.arguments?.getString("id")?.toIntOrNull()
    val firstName = navController.currentBackStackEntry?.arguments?.getString("firstName")
    val familyName = navController.currentBackStackEntry?.arguments?.getString("familyName")
    val photoUrl = navController.currentBackStackEntry?.arguments?.getString("photoUrl")
    val address = navController.currentBackStackEntry?.arguments?.getString("address")
    val phone = navController.currentBackStackEntry?.arguments?.getString("phone")
    val name = navController.currentBackStackEntry?.arguments?.getString("name")
    val mapUrl = navController.currentBackStackEntry?.arguments?.getString("map")

    DoctorDetails(
        navController = navController,
        doctorId = doctorId,
        firstName = firstName,
        familyName = familyName,
        photoUrl = photoUrl,
        address = address,
        mapurl = mapUrl,
        name = name,
        phone = phone
    )
}