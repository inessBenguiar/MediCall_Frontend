package com.example.medicall.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MultiMonthCalendar(
    numberOfMonthsToShow: Int = 24, // Increased to 24 months (2 years)
    bookedDates: Set<String>, // Format "yyyy-MM-dd" like ["2023-05-15"]
    onDateSelected: (date: String) -> Unit, // Returns "yyyy-MM-dd"
    initialMonth: Int = -1 // -1 means use current month
) {
    // Get current date
    val currentCalendar = remember { Calendar.getInstance() }
    val currentYear = currentCalendar.get(Calendar.YEAR)
    val currentMonth = currentCalendar.get(Calendar.MONTH)

    // Create a list of month-year pairs for the number of months we want to show
    val monthsToShow = remember {
        val months = mutableListOf<Pair<Int, Int>>() // Pair of (year, month)
        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, currentYear)
            set(Calendar.MONTH, currentMonth)
        }

        repeat(numberOfMonthsToShow) {
            months.add(Pair(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)))
            calendar.add(Calendar.MONTH, 1)
        }
        months
    }

    val scope = rememberCoroutineScope()
    val initialPage = if (initialMonth >= 0 && initialMonth < numberOfMonthsToShow) initialMonth else 0
    val pagerState = rememberPagerState(initialPage = initialPage) { numberOfMonthsToShow }
    var currentPage by remember { mutableIntStateOf(initialPage) }

    // Calculate max number of rows needed across all months
    val maxNumberOfRows = remember {
        monthsToShow.maxOf { (year, month) ->
            val cal = Calendar.getInstance().apply {
                set(year, month, 1)
            }
            val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
            val firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
            val totalCells = daysInMonth + (firstDayOfWeek - Calendar.SUNDAY)
            (totalCells + 6) / 7 // Ceiling division to get rows
        }
    }

    // Fixed height for calendar content
    val calendarHeight = (maxNumberOfRows * 40 + 80).dp // 40dp per row + padding/header

    Column(modifier = Modifier
        .fillMaxWidth()
        .height(calendarHeight)) {

        // Month navigation header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    if (currentPage > 0) {
                        scope.launch {
                            currentPage--
                            pagerState.animateScrollToPage(currentPage)
                        }
                    }
                },
                enabled = currentPage > 0,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Previous Month",
                    tint = if (currentPage > 0) MaterialTheme.colorScheme.primary else Color.Gray
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Current month and year display
            val (year, month) = monthsToShow[currentPage]
            Text(
                text = "${getMonthName(month)} $year",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(200.dp) // Fixed width for title
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = {
                    if (currentPage < numberOfMonthsToShow - 1) {
                        scope.launch {
                            currentPage++
                            pagerState.animateScrollToPage(currentPage)
                        }
                    }
                },
                enabled = currentPage < numberOfMonthsToShow - 1,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Next Month",
                    tint = if (currentPage < numberOfMonthsToShow - 1) MaterialTheme.colorScheme.primary else Color.Gray
                )
            }
        }

        // Horizontal pager for months
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val (year, month) = monthsToShow[page]

            // Update current page when swiped
            LaunchedEffect(pagerState.currentPage) {
                currentPage = pagerState.currentPage
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((maxNumberOfRows * 40 + 40).dp) // Fixed height for all months
                    .padding(horizontal = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F4FF))
            ) {
                MonthCalendar(
                    year = year,
                    month = month,
                    bookedDates = bookedDates,
                    onDateSelected = onDateSelected,
                    maxNumberOfRows = maxNumberOfRows
                )
            }
        }
    }
}

@Composable
fun MonthCalendar(
    year: Int,
    month: Int, // 0-11 (Calendar.JANUARY to Calendar.DECEMBER)
    bookedDates: Set<String>, // Format "yyyy-MM-dd" like ["2023-05-15"]
    onDateSelected: (date: String) -> Unit, // Returns "yyyy-MM-dd"
    maxNumberOfRows: Int // Pass in the maximum number of rows
) {
    val calendar = remember { Calendar.getInstance().apply {
        set(year, month, 1)
    } }

    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

    Column(modifier = Modifier.padding(16.dp)) {
        // Weekday headers
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Days grid - uses fixed number of rows for consistent height
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.height((maxNumberOfRows * 40).dp),
            userScrollEnabled = false
        ) {
            // Empty cells for days before the 1st
            items((firstDayOfWeek - Calendar.SUNDAY)) {
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(4.dp)
                )
            }

            // Days of the month
            items(daysInMonth) { day ->
                val date = "%04d-%02d-%02d".format(year, month + 1, day + 1)
                val isBooked = bookedDates.contains(date)
                val isWeekend = isWeekend(year, month, day + 1)

                // Check if this date is in the past
                val isPastDate = isPastDate(year, month, day + 1)

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(4.dp)
                        .background(
                            color = when {
                                isPastDate -> Color.LightGray.copy(alpha = 0.5f)
                                isBooked -> Color.Yellow.copy(alpha = 0.5f)
                                isWeekend -> Color.Cyan.copy(alpha = 0.3f)
                                else -> Color.Transparent
                            },
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(
                            enabled = !isBooked && !isPastDate,
                            onClick = { onDateSelected(date) }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (day + 1).toString(),
                        color = when {
                            isPastDate -> Color.Gray
                            else -> MaterialTheme.colorScheme.onSurface
                        },
                        fontSize = 16.sp
                    )
                }
            }

            // Add empty cells at the end to maintain consistency for months with fewer days
            val emptyEndCells = (maxNumberOfRows * 7) - ((firstDayOfWeek - Calendar.SUNDAY) + daysInMonth)
            if (emptyEndCells > 0) {
                items(emptyEndCells) {
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .padding(4.dp)
                    )
                }
            }
        }
    }
}

// Helper function to check if a date is in the past
private fun isPastDate(year: Int, month: Int, day: Int): Boolean {
    val today = Calendar.getInstance()
    val checkDate = Calendar.getInstance().apply {
        set(year, month, day)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    today.set(Calendar.HOUR_OF_DAY, 0)
    today.set(Calendar.MINUTE, 0)
    today.set(Calendar.SECOND, 0)
    today.set(Calendar.MILLISECOND, 0)

    return checkDate.before(today)
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