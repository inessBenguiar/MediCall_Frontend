package com.example.medicall.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar


data class DoctorAvailability(
    val default: WeeklyAvailability = WeeklyAvailability(),
    val exceptions: List<DateAvailability> = emptyList()
)

data class WeeklyAvailability(
    val workingDays: List<Int> = listOf(1, 2, 3, 4, 5), // 1=Monday
    val workingHours: TimeRange = TimeRange("09:00", "17:00"),

)

data class DateAvailability(
    val date: String, // yyyy-MM-dd
    val isWorkingDay: Boolean? = null,
    val workingHours: TimeRange? = null,
    val reason: String? = null
)

data class TimeRange(
    val start: String, // HH:mm
    val end: String
)

// Add these functions to your utility file
fun isWeekend(year: Int, month: Int, day: Int): Boolean {
    val calendar = Calendar.getInstance().apply {
        set(year, month, day)
    }
    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    return dayOfWeek == Calendar.FRIDAY || dayOfWeek == Calendar.SATURDAY
}

// For holidays - you'll need to implement your country's specific holidays
fun isHoliday(year: Int, month: Int, day: Int): Boolean {
    // Example for US holidays (expand as needed)
    return when {
        // New Year's Day
        month == Calendar.JANUARY && day == 1 -> true
        // Independence Day (US)
        month == Calendar.JULY && day == 4 -> true
        // Christmas
        month == Calendar.DECEMBER && day == 25 -> true
        else -> false
    }
}
@Composable
fun CompatibleCalendar(
    year: Int,
    month: Int, // 0-11 (Calendar.JANUARY to Calendar.DECEMBER)
    bookedDates: Set<String>, // Format "yyyy-MM-dd" like ["2023-05-15"]
    onDateSelected: (date: String) -> Unit // Returns "yyyy-MM-dd"
) {
    val calendar = remember { Calendar.getInstance().apply {
        set(year, month, 1)
    } }

    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

    Column(modifier = Modifier.padding(8.dp)) {
        // Header
        Row {
            Text(
                text = "${getMonthName(month)}",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Spacer(modifier = Modifier.width(40.dp))
            Text(
                text = "${year}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        // Weekday headers
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 15.sp,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }

        // Days grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.height(280.dp)
        ) {
            // Empty cells for days before the 1st
            items((firstDayOfWeek - Calendar.SUNDAY)) { }

            // Days of the month
            items(daysInMonth) { day ->
                val date = "%04d-%02d-%02d".format(year, month + 1, day + 1)
                val isBooked = bookedDates.contains(date)
                val isWeekend = isWeekend(year, month, day + 1)

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(4.dp)
                        .background(
                            color = when {
                                isBooked -> Color.Yellow.copy(alpha = 0.5f)
                                isWeekend -> Color.LightGray.copy(alpha = 0.3f)
                                else -> Color.Transparent
                            },
                            shape = RectangleShape,

                            )
                        .clip(RoundedCornerShape(10.dp))
                        .clickable(
                            enabled = !isBooked,
                            onClick = { onDateSelected(date) }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (day + 1).toString(),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

private fun getMonthName(month: Int): String {
    return when (month) {
        Calendar.JANUARY -> "January"
        Calendar.FEBRUARY -> "February"
        Calendar.MARCH -> "March"
        Calendar.APRIL -> "April"
        Calendar.MAY -> "May"
        Calendar.JUNE -> "June"
        Calendar.JULY -> "July"
        Calendar.AUGUST -> "August"
        Calendar.SEPTEMBER -> "September"
        Calendar.OCTOBER -> "October"
        Calendar.NOVEMBER -> "November"
        Calendar.DECEMBER -> "December"
        else -> ""
    }
}
