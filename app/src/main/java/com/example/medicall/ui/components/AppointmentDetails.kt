package com.example.medicall.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.medicall.service.Appointment
import com.example.medicall.service.AppointmentService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppointmentDetails(navController: NavController, appointmentId: Int) {
    var appointment by remember { mutableStateOf<Appointment?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val primaryColor = Color(0xFF1676F3)
    val secondaryColor = Color(0xFF3B8CE4)
    val backgroundColor = Color(0xFFF5F9FF)
    val cardColor = Color.White
    val textPrimaryColor = Color(0xFF2B3A67)
    val textSecondaryColor = Color(0xFF6E798C)
    val accentColor = Color(0xFF18A558)
    val dividerColor = Color(0xFFE5EAF2)

    LaunchedEffect(appointmentId) {
        try {
            val result = withContext(Dispatchers.IO) {
                AppointmentService.createInstance().getAppointmentById(appointmentId)
            }
            appointment = result
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = primaryColor
            )
        } else {
            appointment?.let { appt ->
                Column(modifier = Modifier.fillMaxSize()) {
                    // Top Bar with back navigation
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = primaryColor
                            )
                        }

                        Text(
                            text = "${appt.first_name} ${appt.family_name}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimaryColor
                        )

                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Status Indicator
                    val statusColor = when(appt.status.lowercase()) {
                        "confirmed" -> accentColor
                        "pending" -> Color(0xFFF9A826)
                        "cancelled" -> Color(0xFFE53935)
                        else -> textSecondaryColor
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp)
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = cardColor),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Status",
                                        fontSize = 16.sp,
                                        color = textSecondaryColor
                                    )

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(statusColor.copy(alpha = 0.1f))
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = appt.status.capitalize(),
                                            fontSize = 14.sp,
                                            color = statusColor,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Format date time nicely if possible
                                val formattedDateTime = try {
                                    val dateTime = LocalDateTime.parse(appt.date_time, DateTimeFormatter.ISO_DATE_TIME)
                                    val dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")
                                    val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")

                                    Pair(
                                        dateFormatter.format(dateTime),
                                        timeFormatter.format(dateTime)
                                    )
                                } catch (e: Exception) {
                                    Pair(appt.date_time, "")
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarToday,
                                        contentDescription = "Date",
                                        tint = primaryColor,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = formattedDateTime.first,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = textPrimaryColor
                                    )
                                }

                                if (formattedDateTime.second.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.AccessTime,
                                            contentDescription = "Time",
                                            tint = primaryColor,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = formattedDateTime.second,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = textPrimaryColor
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Patient details card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = cardColor),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = "Patient Information",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = textPrimaryColor
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                            Divider(color = dividerColor)
                            Spacer(modifier = Modifier.height(16.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Patient",
                                    tint = primaryColor,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Full Name",
                                        fontSize = 14.sp,
                                        color = textSecondaryColor
                                    )
                                    Text(
                                        text = "${appt.first_name} ${appt.family_name}",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = textPrimaryColor
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Phone,
                                    contentDescription = "Phone",
                                    tint = primaryColor,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Phone Number",
                                        fontSize = 14.sp,
                                        color = textSecondaryColor
                                    )
                                    Text(
                                        text = appt.phone,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = textPrimaryColor
                                    )
                                }
                            }

                            // Notes section removed as requested
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Reschedule button
                        Button(
                            onClick = { /* Handle reschedule */ },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = secondaryColor.copy(alpha = 0.1f),
                                contentColor = secondaryColor
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = "Reschedule",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Reschedule",
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Button(
                            onClick = { /* Handle cancel */ },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFE53935).copy(alpha = 0.1f),
                                contentColor = Color(0xFFE53935)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Cancel,
                                contentDescription = "Cancel",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Cancel",
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Back button with primary color
                    Button(
                        onClick = {  },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Add prescription",
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            } ?: run {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ErrorOutline,
                        contentDescription = "Error",
                        tint = Color(0xFFE53935),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Appointment Not Found",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = textPrimaryColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "The appointment you're looking for doesn't exist or has been removed.",
                        fontSize = 14.sp,
                        color = textSecondaryColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { navController.popBackStack() },
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Go Back",
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

private fun String.capitalize(): String {
    return this.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}

