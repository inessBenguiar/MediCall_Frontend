package com.example.medicall.ui.components

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.medicall.service.Appointment
import com.example.medicall.service.AppointmentService
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.TemporalField
import org.threeten.bp.temporal.WeekFields

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppointmentDetails(navController: NavController, appointmentId: Int) {
    var appointment by remember { mutableStateOf<Appointment?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    val context= LocalContext.current

    // Theme colors
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

                    val statusLower = appt.status.lowercase()
                    val statusColor = when (statusLower) {
                        "confirmed" -> accentColor
                        "pending" -> Color(0xFFF9A826)
                        "cancelled" -> Color(0xFFE53935)
                        else -> textSecondaryColor
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        colors = CardDefaults.cardColors(containerColor = cardColor),
                        elevation = CardDefaults.cardElevation(1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Status", fontSize = 16.sp, color = textSecondaryColor)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(statusColor.copy(alpha = 0.1f))
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = appt.status.replaceFirstChar { it.uppercase() },
                                        fontSize = 14.sp,
                                        color = statusColor,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            val formattedDateTime = try {
                                val dateTime = LocalDateTime.parse(appt.date_time, DateTimeFormatter.ISO_DATE_TIME)
                                val date = dateTime.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy"))
                                val time = dateTime.format(DateTimeFormatter.ofPattern("h:mm a"))
                                Pair(date, time)
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
                                Text(formattedDateTime.first, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = textPrimaryColor)
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
                                    Text(formattedDateTime.second, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = textPrimaryColor)
                                }
                            }
                        }
                    }

                    // Patient info card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = cardColor),
                        elevation = CardDefaults.cardElevation(1.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text("Patient Information", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = textPrimaryColor)
                            Spacer(modifier = Modifier.height(16.dp))
                            Divider(color = dividerColor)
                            Spacer(modifier = Modifier.height(16.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = primaryColor, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text("Full Name", fontSize = 14.sp, color = textSecondaryColor)
                                    Text("${appt.first_name} ${appt.family_name}", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = textPrimaryColor)
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Phone, contentDescription = null, tint = primaryColor, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text("Phone Number", fontSize = 14.sp, color = textSecondaryColor)
                                    Text(appt.phone, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = textPrimaryColor)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Conditional Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (statusLower == "cancelled") {
                            Button(
                                onClick = { navController.navigate("booking/${appointment!!.doctor_id}") },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = secondaryColor.copy(alpha = 0.1f),
                                    contentColor = secondaryColor
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.Schedule, contentDescription = "Reschedule", modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Reschedule", fontWeight = FontWeight.Medium)
                            }
                        } else if (statusLower == "confirmed") {
                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        try {
                                            val response = withContext(Dispatchers.IO) {
                                                AppointmentService.createInstance().cancelAppointment(appointmentId)
                                            }
                                            if (response.isSuccessful) {
                                                var message = response.message()
                                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                                appointment = appointment?.copy(status = "cancelled")
                                            } else {
                                            }
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                    }
                                },

                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFE53935).copy(alpha = 0.1f),
                                    contentColor = Color(0xFFE53935)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.Cancel, contentDescription = "Cancel", modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Cancel", fontWeight = FontWeight.Medium)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { /* TODO: Handle Add Prescription */ },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Add prescription", color = Color.White, fontWeight = FontWeight.Medium, modifier = Modifier.padding(vertical = 4.dp))
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
                }
            }
        }
    }
}
