package com.example.medicall.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medicall.entity.Medication
import com.example.medicall.viewmodel.PrescriptionViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
@Composable
fun AddEditPrescriptionScreen(
    viewModel: PrescriptionViewModel,
    prescriptionId: Long? = null,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
     var patientName by remember { mutableStateOf("") }
    var patientAge by remember { mutableStateOf("") }
    var diagnosis by remember { mutableStateOf("") }
    var instructionsText by remember { mutableStateOf("") }

    var medications by remember { mutableStateOf<List<Medication>>(emptyList()) }
    var showMedicationForm by remember { mutableStateOf(false) }

    var medicationName by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("14 jours") }
    var medicationInstructions by remember { mutableStateOf("") }

    val currentPrescription by viewModel.currentPrescription.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

   val primaryBlue = Color(0xFF1976D2)

    LaunchedEffect(prescriptionId) {
       if (prescriptionId != currentPrescription?.prescription?.id) {
            patientName = ""
            patientAge = ""
            diagnosis = ""
            instructionsText = ""
            medications = emptyList()

            prescriptionId?.let { id ->
                viewModel.getPrescription(id)
            }
        }
    }

    LaunchedEffect(currentPrescription) {
        currentPrescription?.let { prescriptionWithMeds ->

            if (prescriptionId == prescriptionWithMeds.prescription.id) {
                patientName = prescriptionWithMeds.prescription.patientName
                patientAge = prescriptionWithMeds.prescription.patientAge?.toString() ?: ""
                diagnosis = prescriptionWithMeds.prescription.diagnosis ?: ""
                instructionsText = prescriptionWithMeds.prescription.instructions ?: ""
                medications = prescriptionWithMeds.medications
            }
        }
    }

    fun addMedication() {
        if (medicationName.isBlank() || dosage.isBlank() || frequency.isBlank()) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("Merci de remplir tous les champs obligatoires du médicament")
            }
            return
        }

        val newMedication = Medication(
            id = 0L,
            name = medicationName,
            dosage = dosage,
            frequency = frequency,
            instructions = medicationInstructions.takeIf { it.isNotBlank() },
            duration = duration.takeIf { it.isNotBlank() },
            prescriptionId = prescriptionId ?: 0L
        )

        medications = medications + newMedication


        medicationName = ""
        dosage = ""
        frequency = ""
        medicationInstructions = ""
        duration = "14 jours"
        showMedicationForm = false
    }

   fun removeMedication(medication: Medication) {
        medications = medications.filter { it != medication }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = primaryBlue
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(primaryBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Prescription",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (prescriptionId == null) "Nouvelle Prescription" else "Modifier Prescription",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Remplissez les informations ci-dessous pour créer une prescription.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Nom du Patient*",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                )
                OutlinedTextField(
                    value = patientName,
                    onValueChange = { patientName = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryBlue,
                        unfocusedBorderColor = Color.LightGray
                    ),
                    placeholder = { Text("Entrez le nom du patient") }
                )

                Text(
                    text = "Âge du Patient",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                )
                OutlinedTextField(
                    value = patientAge,
                    onValueChange = { patientAge = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryBlue,
                        unfocusedBorderColor = Color.LightGray
                    ),
                    placeholder = { Text("Entrez l'âge du patient") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Text(
                    text = "Diagnostic",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                )
                OutlinedTextField(
                    value = diagnosis,
                    onValueChange = { diagnosis = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryBlue,
                        unfocusedBorderColor = Color.LightGray
                    ),
                    placeholder = { Text("Entrez le diagnostic") }
                )

                Text(
                    text = "Instructions Générales",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                )
                OutlinedTextField(
                    value = instructionsText,
                    onValueChange = { instructionsText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .height(100.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryBlue,
                        unfocusedBorderColor = Color.LightGray
                    ),
                    placeholder = { Text("Entrez les instructions générales") }
                )

                Spacer(modifier = Modifier.height(8.dp))

               Text(
                    text = "Médicaments",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                if (medications.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Aucun médicament ajouté",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                } else {
                    medications.forEach { medication ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFF5F5F5)
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = medication.name,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Posologie: ${medication.dosage}",
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "Fréquence: ${medication.frequency}",
                                        fontSize = 14.sp
                                    )
                                    medication.duration?.let {
                                        Text(
                                            text = "Durée: $it",
                                            fontSize = 14.sp
                                        )
                                    }
                                    medication.instructions?.let {
                                        Text(
                                            text = "Instructions: $it",
                                            fontSize = 12.sp,
                                            color = Color.DarkGray
                                        )
                                    }
                                }
                                IconButton(
                                    onClick = { removeMedication(medication) },
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Supprimer",
                                        tint = Color.Red
                                    )
                                }
                            }
                        }
                    }
                }

                if (!showMedicationForm) {
                    Button(
                        onClick = { showMedicationForm = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryBlue
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Ajouter",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Ajouter un médicament")
                    }
                }

               if (showMedicationForm) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF8F8F8)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Ajouter un Médicament",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            Text(
                                text = "Nom du Médicament*",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 4.dp)
                            )
                            OutlinedTextField(
                                value = medicationName,
                                onValueChange = { medicationName = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = primaryBlue,
                                    unfocusedBorderColor = Color.LightGray
                                ),
                                placeholder = { Text("Nom du médicament") }
                            )

                            Text(
                                text = "Posologie*",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 4.dp)
                            )
                            OutlinedTextField(
                                value = dosage,
                                onValueChange = { dosage = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = primaryBlue,
                                    unfocusedBorderColor = Color.LightGray
                                ),
                                placeholder = { Text("Ex: 500mg") }
                            )

                            Text(
                                text = "Fréquence*",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 4.dp)
                            )
                            OutlinedTextField(
                                value = frequency,
                                onValueChange = { frequency = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = primaryBlue,
                                    unfocusedBorderColor = Color.LightGray
                                ),
                                placeholder = { Text("Ex: 2 fois par jour") }
                            )

                            Text(
                                text = "Durée",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 4.dp)
                            )
                            OutlinedTextField(
                                value = duration,
                                onValueChange = { duration = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = primaryBlue,
                                    unfocusedBorderColor = Color.LightGray
                                ),
                                placeholder = { Text("Ex: 14 jours") }
                            )

                            Text(
                                text = "Instructions Spécifiques",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 4.dp)
                            )
                            OutlinedTextField(
                                value = medicationInstructions,
                                onValueChange = { medicationInstructions = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)
                                    .height(80.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = primaryBlue,
                                    unfocusedBorderColor = Color.LightGray
                                ),
                                placeholder = { Text("Instructions spécifiques (optionnel)") }
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                OutlinedButton(
                                    onClick = { showMedicationForm = false },
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 8.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    border = ButtonDefaults.outlinedButtonBorder.copy(
                                        brush = androidx.compose.ui.graphics.SolidColor(Color.Gray)
                                    )
                                ) {
                                    Text("Annuler")
                                }
                                Button(
                                    onClick = { addMedication() },
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 8.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = primaryBlue
                                    )
                                ) {
                                    Text("Ajouter")
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (patientName.isBlank()) {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Le nom du patient est requis")
                            }
                            return@Button
                        }

                        if (medications.isEmpty()) {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Veuillez ajouter au moins un médicament")
                            }
                            return@Button
                        }

                        coroutineScope.launch {

                            val dateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                .format(Date())


                            viewModel.savePrescription(
                                id = prescriptionId,
                                patientName = patientName,
                                diagnosis = diagnosis,
                                instructions = instructionsText,
                                date = dateString,
                                medications = medications,
                                patientAge = patientAge
                            )


                            viewModel.clearCurrentPrescription()
                            onBack()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryBlue
                    )
                ) {
                    Text(
                        text = if (prescriptionId == null) "Créer Prescription" else "Mettre à jour",
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))


                TextButton(
                    onClick = {
                        viewModel.clearCurrentPrescription()
                        onBack()
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = "Retour",
                        color = primaryBlue
                    )
                }
            }
        }


        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}