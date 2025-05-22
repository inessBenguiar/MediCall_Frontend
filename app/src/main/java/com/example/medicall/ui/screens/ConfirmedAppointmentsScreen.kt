package com.example.medicall.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.medicall.R
import com.example.medicall.entity.ConfirmedAppointment
import com.example.medicall.viewmodel.AppointmentViewModel

@Composable
fun ConfirmedAppointmentsScreen(
    patientId: Int,
    navController: NavController,
    viewModel: AppointmentViewModel = viewModel()
) {
    var selectedTab by remember { mutableStateOf(0) }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val screenWidth = configuration.screenWidthDp.dp
    val isTablet = screenWidth >= 600.dp

    LaunchedEffect(patientId) {
        viewModel.fetchConfirmedAppointments(patientId)
    }

    val appointments by viewModel.confirmedAppointments.collectAsState(initial = emptyList())
    val error by viewModel.confirmedAppointmentsError.collectAsState(initial = null)

    val doctorImage = painterResource(id = R.drawable.doctor1)

    // Map ConfirmedAppointment to AppointmentData using only available fields
    val appointmentDataList = appointments.map { appt ->
        AppointmentData(
            doctorImage = doctorImage,
            doctorName = "Dr. ${appt.first_name ?: ""} ${appt.family_name ?: ""}".trim(),
            date = appt.date_time.split("T").getOrNull(0) ?: appt.date_time,
            time = appt.date_time.split("T").getOrNull(1) ?: "Time unknown"
        )
    }

    Scaffold(
        bottomBar = {
            if (!isLandscape || !isTablet) {
                // Place bottom navigation if needed
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = "My Appointments",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = if (isTablet) 24.sp else 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF374151)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = if (isTablet) 24.dp else 16.dp,
                        vertical = if (isLandscape) 4.dp else 8.dp
                    )
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(4.dp))

            val selectedColor = Color(0xFF1676F3)
            val unselectedColor = Color(0xFF9CA3AF)

            TabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color.Transparent,
                divider = {},
                indicator = { tabPositions ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.BottomStart)
                            .offset(x = tabPositions[selectedTab].left)
                            .width(tabPositions[selectedTab].width)
                            .height(3.dp)
                            .background(selectedColor)
                    )
                }
            ) {
                listOf("Upcoming", "Completed", "Canceled").forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selectedTab == index) selectedColor else unselectedColor
                            )
                        }
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = if (isTablet) 24.dp else 16.dp, vertical = 8.dp)
            ) {
                when {
                    error != null -> {
                        Text("Error: $error", color = MaterialTheme.colorScheme.error)
                    }
                    appointments.isEmpty() -> {
                        Text("No appointments found.")
                    }
                    else -> {
                        when (selectedTab) {
                            0 -> UpcomingAppointments(appointmentDataList, navController)
                            1 -> CompletedAppointments()
                            2 -> CanceledAppointments()
                        }
                    }
                }
            }
        }
    }
}

data class AppointmentData(
    val doctorImage: Painter,
    val doctorName: String,
    val date: String,
    val time: String
)

@Composable
fun UpcomingAppointments(
    appointmentList: List<AppointmentData>,
    navController: NavController
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(appointmentList) { appointment ->
            AppointmentCard(
                doctorImage = appointment.doctorImage,
                doctorName = appointment.doctorName,
                date = appointment.date,
                time = appointment.time,
                onCancel = {
                    // TODO: cancel logic here
                },
                onReschedule = {
                    // TODO: reschedule logic here
                },
                onQrCodeClick = {
                    navController.navigate("qr_code/${appointment.date}-${appointment.time}")
                }
            )
        }
    }
}

@Composable
fun CompletedAppointments() {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Completed appointments will appear here")
    }
}

@Composable
fun CanceledAppointments() {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Canceled appointments will appear here")
    }
}

@Composable
fun AppointmentCard(
    doctorImage: Painter,
    doctorName: String,
    date: String,
    time: String,
    onCancel: () -> Unit,
    onReschedule: () -> Unit,
    onQrCodeClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("$date - $time", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Icon(
                    painter = painterResource(id = R.drawable.qricon1),
                    contentDescription = "QR Code",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onQrCodeClick() },
                    tint = Color.Black
                )
            }

            Divider(
                color = Color(0xFFE5E7EB),
                thickness = 0.5.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = doctorImage,
                    contentDescription = "Doctor Image",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(doctorName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    // Removed specialty, clinic, location since not in data model
                }
            }

            Divider(
                color = Color(0xFFE5E7EB),
                thickness = 0.5.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = onCancel,
                    border = BorderStroke(1.dp, Color(0xFF1676F3)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF1676F3)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                ) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = onReschedule,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1676F3)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                ) {
                    Text("Reschedule", color = Color.White)
                }
            }
        }
    }
}
