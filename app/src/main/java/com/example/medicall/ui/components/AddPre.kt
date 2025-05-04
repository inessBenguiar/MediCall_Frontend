package com.example.medicall.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.medicall.service.PrescriptionRequest
import com.example.medicall.service.PrescriptionService
import retrofit2.Call

@Composable
fun AddPrescriptionForm(navController: NavController) {
    var appointmentId by remember { mutableStateOf("") }
    var diagnosis by remember { mutableStateOf("") }
    var instructions by remember { mutableStateOf("") }

    val context = LocalContext.current
    val prescriptionService = PrescriptionService.createInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            "Add Prescription",
            fontSize = 24.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
        )
        Text(
            "Fill in the details below.",
            fontSize = 16.sp,
            color = androidx.compose.ui.graphics.Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = appointmentId,
            onValueChange = { appointmentId = it },
            label = { Text("Appointment ID") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = diagnosis,
            onValueChange = { diagnosis = it },
            label = { Text("Diagnosis") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = instructions,
            onValueChange = { instructions = it },
            label = { Text("Instructions") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp), // Plus haut pour les instructions longues
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                if (appointmentId.isBlank() || diagnosis.isBlank() || instructions.isBlank()) {
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                } else {
                    val prescription = PrescriptionRequest(
                        appointment_id = appointmentId.toInt(),
                        diagnosis = diagnosis,
                        instructions = instructions
                    )

                    // Appel HTTP classique avec enqueue()
                    prescriptionService.addPrescription(prescription).enqueue(object : retrofit2.Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: retrofit2.Response<Void>) {
                            if (response.isSuccessful) {
                                Toast.makeText(context, "Prescription added successfully!", Toast.LENGTH_SHORT).show()
                                navController.navigate("prescriptionList")
                            } else {
                                Toast.makeText(context, "Failed to add prescription: ${response.code()}", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Toast.makeText(context, "Error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color(0xFF1676F3)),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Prescription", color = androidx.compose.ui.graphics.Color.White)
        }
}}