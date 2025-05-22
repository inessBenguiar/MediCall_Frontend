package com.example.medicall.ui.screens  // MUST be the very first line

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.medicall.repository.AppointmentRepositoryImpl
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrCodeScreen(appointmentId: Int, navController: NavController) {
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
                    val bitmap = remember(code) { generateQrCode(code) }
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "QR Code",
                        modifier = Modifier.size(250.dp)
                    )
                } ?: run {
                    Text("No QR code available")
                }
            }
        }
    }
}

// QR Code generator function using ZXing
fun generateQrCode(content: String, size: Int = 512): Bitmap {
    val bitMatrix: BitMatrix = MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, size, size)
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
    for (x in 0 until size) {
        for (y in 0 until size) {
            bitmap.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
        }
    }
    return bitmap
}
