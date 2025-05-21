package com.example.medicall.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.medicall.repository.AppointmentRepositoryImpl
import com.example.medicall.service.Endpoint
import com.example.medicall.ui.components.BookScreen
import com.example.medicall.viewmodel.AppointmentViewModel

@Composable
fun Booking (){

    // Initialize the Repository
    val repository = AppointmentRepositoryImpl()

    // Initialize the ViewModel
    val viewModel = AppointmentViewModel(repository)

    // Pass the ViewModel to the BookScreen
    BookScreen(viewModel = viewModel)
}