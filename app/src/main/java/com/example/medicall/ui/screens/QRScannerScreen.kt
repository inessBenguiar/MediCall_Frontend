package com.example.medicall.ui.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.journeyapps.barcodescanner.ScanOptions
import com.journeyapps.barcodescanner.ScanContract

@Composable
fun QRScannerScreen(onAppointmentScanned: (String) -> Unit) {
    val context = LocalContext.current

    val barcodeLauncher = rememberLauncherForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            Toast.makeText(context, "Scanned: ${result.contents}", Toast.LENGTH_SHORT).show()
            // Callback with the scanned appointmentId
            onAppointmentScanned(result.contents)
        } else {
            Toast.makeText(context, "Scan cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Scan Patient's QR Code", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            val options = ScanOptions()
            options.setPrompt("Scan patient's QR Code")
            options.setBeepEnabled(true)
            options.setOrientationLocked(true)
            options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
            barcodeLauncher.launch(options)
        }) {
            Text("Start Scanning")
        }
    }
}
