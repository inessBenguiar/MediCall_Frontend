package com.example.medicall.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.medicall.ui.components.DoctorDetails

@Composable
fun DoctorInfo(navController: NavController, navBackStackEntry: NavBackStackEntry) {
    val firstName = navBackStackEntry.arguments?.getString("firstName")
    val familyName = navBackStackEntry.arguments?.getString("familyName")
    val photoUrl = navBackStackEntry.arguments?.getString("photoUrl")
    val address = navBackStackEntry.arguments?.getString("address")
    val phone = navBackStackEntry.arguments?.getString("phone")

    DoctorDetails(
        navController = navController,
        firstName = firstName,
        familyName = familyName,
        photoUrl = photoUrl,
        address = address,
        phone = phone
    )
}
