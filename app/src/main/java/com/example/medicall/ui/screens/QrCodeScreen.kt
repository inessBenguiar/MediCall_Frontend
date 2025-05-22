package com.example.medicall.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.medicall.repository.AppointmentRepository
import com.example.medicall.entity.ConfirmedAppointment  // Your data model import
import com.example.medicall.repository.AppointmentRepositoryImpl
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun QrCodeScreen(appointmentId: Int, navController: NavController) {
    // Use the implementation class instance here:
    val appointmentRepository = remember { AppointmentRepositoryImpl() }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var qrCodeText by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(appointmentId) {
        try {
            val result = appointmentRepository.getAppointmentById(appointmentId)
            if (result.isSuccess) {
                qrCodeText = result.getOrNull()?.qr_code
            } else {
                scope.launch {
                    snackbarHostState.showSnackbar("Failed to load QR code: ${result.exceptionOrNull()?.message}")
                }
            }
        } catch (e: Exception) {
            scope.launch {
                snackbarHostState.showSnackbar("Failed to load QR code: ${e.message}")
            }
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Appointment QR Code") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                qrCodeText?.let { code ->
                    Text(text = code, style = MaterialTheme.typography.headlineMedium)
                    // TODO: Replace this Text with actual QR code image generation if needed
                } ?: run {
                    Text("No QR code available")
                }
            }
        }
    }
}

