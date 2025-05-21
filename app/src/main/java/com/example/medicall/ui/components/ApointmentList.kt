package com.example.medicall.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.util.*
import androidx.navigation.NavController
import com.example.medicall.service.Appointment
import com.example.medicall.service.AppointmentService
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.TemporalField
import org.threeten.bp.temporal.WeekFields
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppointmentsList(navController: NavController, userId: Int) {
    var selectedFilter by remember { mutableStateOf("All") }
    var appointments by remember { mutableStateOf<List<Appointment>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }


    LaunchedEffect(userId) {
        try {
            val service = AppointmentService.createInstance()
            val result = service.getAppointmentsByDoctor(userId)
            appointments = result
        } catch (e: Exception) {
            e.printStackTrace()
        }
        isLoading = false
    }

    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val filteredAppointments = when (selectedFilter) {
        "All" -> appointments
        "Today" -> appointments.filter {
            LocalDate.parse(it.date_time.substring(0, 10), formatter) == today
        }
        "This week" -> {
            val currentWeek = today.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear())
            appointments.filter {
                val appointmentDate = LocalDate.parse(it.date_time.substring(0, 10), formatter)
                appointmentDate.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()) == currentWeek &&
                        appointmentDate.year == today.year
            }
        }
        else -> emptyList()
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(0xFF1676F3))
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            FilterBar(
                filters = listOf("All", "Today", "This week"),
                selectedFilter = selectedFilter,
                onFilterSelected = { selectedFilter = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {
                items(filteredAppointments) { appointment ->
                    AppointmentCard(
                        appointment = appointment,
                        onClick = { selectedAppointment ->
                            navController.navigate("appointmentDetails/${selectedAppointment.id}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FilterBar(
    filters: List<String>,
    selectedFilter: String,
    onFilterSelected: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        filters.forEach { filter ->
            Button(
                onClick = { onFilterSelected(filter) },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (filter == selectedFilter) Color(0xFF1676F3) else Color(0xFFE8F4FF),
                    contentColor = if (filter == selectedFilter) Color.White else Color(0xFF1676F3)
                )
            ) {
                Text(text = filter, fontSize = 14.sp)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppointmentCard(appointment: Appointment, onClick: (Appointment) -> Unit) {
    val textSecondaryColor = Color(0xFF6E798C)

    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(appointment) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${appointment.first_name} ${appointment.family_name}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Formater la date
            val parsedDateTime = LocalDateTime.parse(appointment.date_time)
            val formattedDate = parsedDateTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy - HH:mm"))

            Text(
                text = formattedDate,
                fontSize = 14.sp,
                color = textSecondaryColor
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Phone",
                    tint = textSecondaryColor,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = appointment.phone,
                    fontSize = 13.sp,
                    color = textSecondaryColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
