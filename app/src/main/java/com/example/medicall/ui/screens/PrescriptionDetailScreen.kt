package com.example.medicall.ui.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medicall.entity.Medication
import com.example.medicall.viewmodel.PrescriptionViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrescriptionDetailScreen(
    viewModel: PrescriptionViewModel,
    prescriptionId: Long,
    onBack: () -> Unit,
    onEdit: (Long) -> Unit
) {
    val currentPrescription by viewModel.currentPrescription.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val primaryBlue = Color(0xFF2196F3)
    val lightBlue = Color(0xFFE3F2FD)
    val backgroundColor = Color(0xFFF8F9FA)
    val isPdfLoading by viewModel.isPdfLoading.collectAsState()
    val pdfDownloadStatus by viewModel.pdfDownloadStatus.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(prescriptionId) {
        viewModel.getPrescription(prescriptionId)
    }
    LaunchedEffect(pdfDownloadStatus) {
        if (pdfDownloadStatus != null) {
            snackbarHostState.showSnackbar(
                message = pdfDownloadStatus!!,
                duration = SnackbarDuration.Short
            )
        }
    }
    Scaffold(
        containerColor = backgroundColor,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Détails Prescription",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    // PDF Download button
                    IconButton(
                        onClick = {
                            scope.launch {
                                viewModel.downloadPrescriptionPdf(prescriptionId, context)
                            }
                        },
                        enabled = !isPdfLoading
                    ) {
                        if (isPdfLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.PictureAsPdf,
                                contentDescription = "Download PDF"
                            )
                        }
                    }

                    // Edit button
                    IconButton(
                        onClick = {
                            onEdit(prescriptionId)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Prescription"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        floatingActionButton = {
            if (!isLoading && currentPrescription != null) {
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            viewModel.downloadPrescriptionPdf(prescriptionId, context)
                        }
                    },
                    containerColor = primaryBlue,
                    contentColor = Color.White
                ) {
                    if (isPdfLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Download Prescription PDF"
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Loading state
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = primaryBlue)
                }
            }
            // Error state
            else if (error != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Erreur",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Red
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = error ?: "Une erreur est survenue",
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.getPrescription(prescriptionId) }) {
                            Text("Réessayer")
                        }
                    }
                }
            }
            // Content when prescription is loaded
            else if (currentPrescription != null) {
                val prescription = currentPrescription!!.prescription
                val medications = currentPrescription!!.medications

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Patient information section
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Avatar with first letter
                                    Box(
                                        modifier = Modifier
                                            .size(60.dp)
                                            .clip(CircleShape)
                                            .background(primaryBlue.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = prescription.patientName.firstOrNull()?.toString() ?: "P",
                                            color = primaryBlue,
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(16.dp))

                                    Column {
                                        Text(
                                            text = prescription.patientName,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))

                                        Text(
                                            text = "Âge: ${prescription.patientAge ?: "N/A"}",
                                            fontSize = 16.sp,
                                            color = Color.Gray
                                        )

                                        val formattedDate = try {
                                            val date = if (prescription.date is String)
                                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(prescription.date.toString())
                                            else
                                                prescription.date as Date

                                            SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date)
                                        } catch (e: Exception) {
                                            prescription.date.toString()
                                        }

                                        Text(
                                            text = "Date: $formattedDate",
                                            fontSize = 16.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Diagnosis section
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "Diagnostic",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = primaryBlue
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = prescription.diagnosis ?: "Aucun diagnostic fourni",
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }

                    // General instructions section
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "Instructions générales",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = primaryBlue
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = prescription.instructions ?: "Aucune instruction fournie",
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }

                    // Medications section
                    item {
                        Text(
                            text = "Médicaments (${medications.size})",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    // List of medications
                    items(medications) { medication ->
                        MedicationCard(medication, primaryBlue)
                    }

                    // Bottom padding
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
            // No prescription loaded
            else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Aucune prescription trouvée",
                        fontSize = 18.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun MedicationCard(medication: Medication, primaryColor: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Medication,
                    contentDescription = null,
                    tint = primaryColor
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = medication.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Medication details
            val details = listOf(
                "Dosage" to medication.dosage,
                "Fréquence" to medication.frequency,
                "Durée" to (medication.duration ?: "Non spécifiée"),
                "Instructions" to (medication.instructions ?: "Aucune instruction")
            )

            details.forEach { (label, value) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        text = "$label: ",
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                    Text(
                        text = value,
                        fontSize = 16.sp
                    )
                }
            }

            // Special instructions if any
            medication.specialInstructions?.let {
                if (it.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Instructions spéciales:",
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                    Text(
                        text = it,
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}