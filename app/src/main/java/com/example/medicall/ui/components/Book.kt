package com.example.medicall.ui.components

import BookAppointmentRequest
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItemDefaults.contentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.medicall.ui.preferences.readId
import com.example.medicall.viewmodel.AppointmentViewModel

@Composable
fun TimeSlotSelection(
    date: String,
    slots: List<Int>, // Change to List<Int> to represent the slot numbers
) {

    var showAllSlots by remember { mutableStateOf(false) }
    val visibleSlots = if (showAllSlots) slots else slots.take(4)
    if (slots.isEmpty()) {
        Text("No available slots", color = MaterialTheme.colorScheme.error)
    } else {
        if (slots.size > 4 && !showAllSlots) {
            TextButton(onClick = { showAllSlots = true }) {
                Text("See All (${slots.size})")
            }
        }
        LazyRow {
            items(visibleSlots) { slot ->
                var selected = false
                OutlinedButton(
                    onClick = { selected = true },
                    modifier = Modifier.padding(horizontal = 4.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = if (selected) Color.Blue
                        else MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(convertSlotToTime(slot)) // Use the conversion function here
                }
            }
        }
        if (showAllSlots) {
            FullScreenTimeSlotsDialog(
                slots = slots.map { convertSlotToTime(it) },
                onDismiss = { showAllSlots = false },
                onSlotSelected = { time ->
                    showAllSlots = false
                }
            )
        }
    }
}

// Conversion Function
fun convertSlotToTime(slot: Int): String {
    val hour = slot / 2
    val minutes = (slot % 2) * 30
    val period = if (hour < 12) "AM" else "PM"
    val formattedHour = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
    return String.format("%02d:%02d %s", formattedHour, minutes, period)
}
@Composable
fun FullScreenTimeSlotsDialog(
    slots: List<String>,
    onDismiss: () -> Unit,
    onSlotSelected: (String) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "All Available Slots",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Time slots grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(slots) { slot ->
                        TimeSlotButton(
                            time = slot,
                            onClick = { onSlotSelected(slot) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TimeSlotButton(
    time: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        shape = RoundedCornerShape(8.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        )
    ) {
        Text(text = time)
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookScreen(viewModel: AppointmentViewModel, navController: NavController,doctorId: Int) {
    var message by remember { mutableStateOf("") }
    // State for selected date

    var selectedDate by remember { mutableStateOf<String?>(null) }
    var slotChoosing by remember { mutableStateOf<Int?>(null) }
    var slotClick by remember { mutableStateOf<Boolean>(false) }
    val context = LocalContext.current
    // Collect available slots and booking status from ViewModel
    val availableSlots by viewModel.availableSlots.collectAsState()
    val bookingStatus by viewModel.bookingStatus.collectAsState()
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Book Appointment",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFF1976D2)
                )
            )
        }
    ) {  paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        ) {
            Spacer(modifier = Modifier.height(90.dp))
            // Calendar
            MultiMonthCalendar(
                bookedDates = setOf("2025-05-15", "2025-05-11"), // Sample booked dates
                onDateSelected = { date ->
                    selectedDate = date
                    viewModel.fetchAvailableSlots(
                        doctorId = doctorId,
                        date = date
                    ) // Fetch slots for selected date
                }
            )
            Spacer(modifier = Modifier.height(40.dp))
            selectedDate?.let {
                Text(
                    text = "Available Slots for $it",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            // Display available slots
            if (availableSlots.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    items(availableSlots.size) { index ->
                        OutlinedButton(
                            onClick = {
                                slotChoosing = availableSlots[index].slot
                                slotClick = !slotClick
                            },
                            modifier = Modifier.padding(4.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (slotChoosing == availableSlots[index].slot && slotClick) {
                                    Color(0xFF007AFF) // Selected state
                                } else {
                                    Color.Transparent // Default state
                                },
                                contentColor = if (slotChoosing == availableSlots[index].slot && slotClick) {
                                    Color.White // Selected text color
                                } else {
                                    Color.Black
                                }
                            )
                        ) {

                            Text(text = availableSlots[index].time)
                        }
                    }
                }
            } else if (selectedDate != null) {
                Text(
                    text = "No available slots",
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
            if (slotChoosing != null) {
                Button(
                    onClick = {
                        if (selectedDate != null) {
                            viewModel.bookAppointment(
                                BookAppointmentRequest(
                                    doctorId = doctorId,
                                    patientId = readId(context)!!.toInt(),
                                    date = selectedDate!!,
                                    slotPosition = slotChoosing!!,
                                )
                            )
                            viewModel.fetchAvailableSlots(doctorId = 26, date = selectedDate!!);
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF007AFF),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth().height(45.dp)
                ) {
                    Text(text = "Book Appointment")
                }
                Spacer(modifier = Modifier.height(40.dp))


            }

            var lastShownStatus by remember { mutableStateOf<String?>(null) }
            // Show booking status
            LaunchedEffect(bookingStatus) {
                bookingStatus?.takeIf { it != lastShownStatus }?.let { status ->
                    Toast.makeText(context, status, Toast.LENGTH_SHORT).show()
                    lastShownStatus = status
                }
            }
        }
    }
}