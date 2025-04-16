package com.example.medicall.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.Surface
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import java.util.Calendar

@Composable
fun TimeSlotSelection(
    date: String,
    slots: List<String>,
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
                    Text(slot)
                }
            }
        }
        if (showAllSlots) {
            FullScreenTimeSlotsDialog(
                slots = slots,
                onDismiss = { showAllSlots = false },
                onSlotSelected = { time ->
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
    @Composable
    fun Book (navController: NavController) {
        val screenHeight = LocalConfiguration.current.screenHeightDp.dp

        var selectedDate by remember { mutableStateOf<String?>(null) }
        //var availableSlots by remember { mutableStateOf<List<String>>(emptyList()) }
        val availableSlots = remember {
            listOf(
                "09:00", "09:30", "10:00", "10:30",
                "14:00", "14:30", "15:00", "15:30"
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight)
                .padding(16.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Go back Arrow",
                    modifier = Modifier
                        .requiredSize(size = 30.dp)
                )
                Text(
                    text = "Select Date and Time",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp), // Add some spacing
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(30.dp))

            }
            val calendar = Calendar.getInstance()
            val currentYear = calendar.get(Calendar.YEAR)
            val currentMonth = calendar.get(Calendar.MONTH) // 0-11

            // Sample booked dates (3 dates in the current month)
            val bookedDates = setOf(
                "%04d-%02d-15".format(currentYear, currentMonth + 1),
                "%04d-%02d-20".format(currentYear, currentMonth + 1),
                "%04d-%02d-25".format(currentYear, currentMonth + 1)
            )

            CompatibleCalendar(
                year = currentYear,
                month = currentMonth,
                bookedDates = bookedDates,
                onDateSelected = { date ->
                    println("Selected date: $date")
                    selectedDate = date
                }
            )
            Text(
                text = "Available Time SLots",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp), // Add some spacing
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            //Display the Available time Slots
            if (selectedDate != null) {
                TimeSlotSelection(
                    date = selectedDate!!,
                    slots = availableSlots,
                )
            }
            Button(
                onClick = {},

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF007AFF), // Set the background color here
                    contentColor = Color.White // You can set the content (text) color
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(
                        RoundedCornerShape(10.dp)
                    )
            ) {

                Text(
                    "Book an Appointment",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }
        }
    }

