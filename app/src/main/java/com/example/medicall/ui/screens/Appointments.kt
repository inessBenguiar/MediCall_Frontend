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
import androidx.navigation.NavController
import com.example.app.components.Navbar
import com.example.medicall.R

@Composable
fun Appointments(navController: NavController) {
    var selectedItem by remember { mutableStateOf(0) }
    var selectedTab by remember { mutableStateOf(0) }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val screenWidth = configuration.screenWidthDp.dp
    val isTablet = screenWidth >= 600.dp

    Scaffold(
        bottomBar = {
            if (!isLandscape || !isTablet) {
                Navbar(selectedItem) { newIndex ->
                    selectedItem = newIndex
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Header
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
                when (selectedTab) {
                    0 -> UpcomingAppointments(isLandscape, isTablet, navController)
                    1 -> CompletedAppointments()
                    2 -> CanceledAppointments()
                }
            }
        }
    }
}

data class AppointmentData(
    val doctorImage: Painter,
    val doctorName: String,
    val specialty: String,
    val clinic: String,
    val location: String,
    val date: String,
    val time: String
)

@Composable
fun UpcomingAppointments(
    isLandscape: Boolean,
    isTablet: Boolean,
    navController: NavController
) {
    val doctor1 = painterResource(id = R.drawable.doctor1)
    val doctor2 = painterResource(id = R.drawable.doctor2)

    val appointments = listOf(
        AppointmentData(doctor1, "Dr. James Robinson", "Orthopedic Surgery", "Elite Ortho Clinic", "USA", "May 22, 2023", "10:00 AM"),
        AppointmentData(doctor2, "Dr. Daniel Lee", "Gastroenterologist", "Digestive Institute", "USA", "June 14, 2023", "15:00 PM"),
        // Add more appointments as needed
    )

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(appointments) { appointment ->
            AppointmentCard(
                doctorImage = appointment.doctorImage,
                doctorName = appointment.doctorName,
                specialty = appointment.specialty,
                clinic = appointment.clinic,
                location = appointment.location,
                date = appointment.date,
                time = appointment.time,
                onCancel = { /* Handle cancel */ },
                onReschedule = { /* Handle reschedule */ },
                onQrCodeClick = {
                    navController.navigate("qr_code/${appointment.date}-${appointment.time}")
                }
            )
        }
    }
}

@Composable
fun CompletedAppointments() {
    Text("Completed appointments will appear here")
}

@Composable
fun CanceledAppointments() {
    Text("Canceled appointments will appear here")
}

@Composable
fun AppointmentCard(
    doctorImage: Painter,
    doctorName: String,
    specialty: String,
    clinic: String,
    location: String,
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
            // Date & QR Code Row
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

            // Doctor Info
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
                    Text(specialty, color = Color.Gray, fontSize = 14.sp)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = "Location",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("$clinic, $location", color = Color.Gray, fontSize = 12.sp)
                    }
                }
            }

            Divider(
                color = Color(0xFFE5E7EB),
                thickness = 0.5.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color(0xFFE5E7EB),
                        contentColor = Color.Black
                    ),
                    border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                ) {
                    Text("Cancel")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = onReschedule,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1676F3),
                        contentColor = Color.White
                    )
                ) {
                    Text("Reschedule")
                }
            }
        }
    }
}
