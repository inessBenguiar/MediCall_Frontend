package com.example.medicall.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.medicall.entity.ConfirmedAppointment
import com.example.medicall.viewmodel.AppointmentViewModel

@Composable
fun ConfirmedAppointmentsScreen(
    patientId: Int,
    viewModel: AppointmentViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    // trigger load once
    LaunchedEffect(patientId) {
        viewModel.fetchConfirmedAppointments(patientId)
    }

    val appointments by viewModel.confirmedAppointments.collectAsState()
    val error       by viewModel.confirmedAppointmentsError.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Your Confirmed Appointments", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(8.dp))

        when {
            error != null -> {
                Text("Error: $error", color = MaterialTheme.colorScheme.error)
            }
            appointments.isEmpty() -> {
                Text("No confirmed appointments found.")
            }
            else -> {
                LazyColumn {
                    items(appointments) { appt ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(Modifier.padding(12.dp)) {
                                Text("Dr. ${appt.first_name} ${appt.family_name}")
                                Text("Date: ${appt.date_time}")
                                Text("Phone: ${appt.phone ?: "N/A"}")
                                Text("Status: ${appt.status}")
                            }
                        }
                    }
                }
            }
        }
    }
}
