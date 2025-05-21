package com.example.medicall.ui.components

import BookAppointmentRequest
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.medicall.viewmodel.AppointmentViewModel

// Convert slot number to human-readable time string
fun convertSlotToTime(slot: Int): String {
    val hour = slot / 2
    val minutes = (slot % 2) * 30
    val period = if (hour < 12) "AM" else "PM"
    val formattedHour = when {
        hour == 0 -> 12
        hour > 12 -> hour - 12
        else -> hour
    }
    return String.format("%02d:%02d %s", formattedHour, minutes, period)
}

@Composable
fun TimeSlotSelection(
    slots: List<Int>,
    onSlotSelected: (Int) -> Unit
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

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(visibleSlots) { slot ->
                OutlinedButton(
                    onClick = { onSlotSelected(slot) },
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    Text(convertSlotToTime(slot))
                }
            }
        }

        if (showAllSlots) {
            FullScreenTimeSlotsDialog(
                slots = slots.map { convertSlotToTime(it) },
                onDismiss = { showAllSlots = false },
                onSlotSelected = { timeString ->
                    val slotIndex = slots.indexOfFirst { convertSlotToTime(it) == timeString }
                    if (slotIndex != -1) {
                        onSlotSelected(slots[slotIndex])
                    }
                    showAllSlots = false
                }
            )
        }
    }
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
            Column(modifier = Modifier.padding(16.dp)) {
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

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(slots) { slot ->
                        TimeSlotButton(time = slot, onClick = { onSlotSelected(slot) })
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
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp, pressedElevation = 4.dp)
    ) {
        Text(text = time)
    }
}

@Composable
fun BookScreen(viewModel: AppointmentViewModel) {
    var selectedDate by remember { mutableStateOf<String?>(null) }
    var selectedSlot by remember { mutableStateOf<Int?>(null) }
    val context = LocalContext.current

    val availableSlots by viewModel.availableSlots.collectAsState()
    val bookingStatus by viewModel.bookingStatus.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Book an Appointment",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 10.dp)
        )

        // Example MultiMonthCalendar usage (implement or replace with your calendar composable)
        MultiMonthCalendar(
            bookedDates = setOf("2025-05-15", "2025-05-11"),
            onDateSelected = { date ->
                selectedDate = date
                viewModel.fetchAvailableSlots(doctorId = 26, date = date)
                selectedSlot = null // reset selected slot on date change
            }
        )

        selectedDate?.let {
            Text(
                text = "Available Slots for $it",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
        }

        if (selectedDate != null) {
            if (availableSlots.isNotEmpty()) {
                TimeSlotSelection(
                    slots = availableSlots.map { it.slot },
                    onSlotSelected = { slot ->
                        selectedSlot = slot
                    }
                )
            } else {
                Text(
                    "No available slots",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (selectedSlot != null && selectedDate != null) {
            Button(
                onClick = {
                    viewModel.bookAppointment(
                        BookAppointmentRequest(
                            doctorId = 26,
                            patientId = 1,
                            date = selectedDate!!,
                            slotPosition = selectedSlot!!
                        )
                    )
                    // Optionally refresh slots after booking
                    viewModel.fetchAvailableSlots(doctorId = 26, date = selectedDate!!)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF007AFF),
                    contentColor = Color.White
                )
            ) {
                Text(text = "Book Appointment")
            }
        }

        bookingStatus?.let { status ->
            LaunchedEffect(status) {
                Toast.makeText(context, status, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
