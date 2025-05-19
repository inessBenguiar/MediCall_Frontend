package com.example.medicall.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.medicall.entity.ClinicResponse
import java.io.File
import java.io.FileOutputStream
import java.util.*

data class ClinicInfo(
    val id: Int? = null,
    val name: String = "",
    val address: String = "",
    val mapLocation: String = "",
)

// Location data class
data class Location(
    val latitude: Double,
    val longitude: Double
)

data class BreakTime(
    var start: String = "",
    var end: String = ""
)

data class WorkingDay(
    val day: String,
    var isWorking: Boolean = false,
    var startTime: String = "",
    var endTime: String = ""
)

data class UserInfo(
    val id : Int = 26,
    val specialty: String = "",
    val phoneNumber: String = "",
    val instagramUrl: String = "",
    val linkedInUrl: String = "",
    val facebookUrl: String = "",
    val photoUri: Uri? = null,
    val experience: String = "",
    val clinic: ClinicInfo = ClinicInfo(),
    val worksOnWeekend: Boolean = false,
    val workEveryday: Boolean = false,
    val startWorkTime: String = "",
    val endWorkTime: String = "",
    val breakTime: BreakTime = BreakTime(),
    val hasInstagram: Boolean = false,
    val hasLinkedIn: Boolean = false,
    val hasFacebook: Boolean = false,
    val workingDays: List<WorkingDay> = listOf(
        WorkingDay("Sunday"),
        WorkingDay("Monday"),
        WorkingDay("Tuesday"),
        WorkingDay("Wednesday"),
        WorkingDay("Thursday"),
        WorkingDay("Friday"),
        WorkingDay("Saturday")
    )
)

@Composable
fun UserInformationScreen() {
    var userInfo by remember { mutableStateOf(UserInfo()) }
    val context = LocalContext.current

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(it)
            val file = File(context.filesDir, "profile_${UUID.randomUUID()}.jpg")
            val outputStream = FileOutputStream(file)
            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            userInfo = userInfo.copy(photoUri = Uri.fromFile(file))
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Professional Information",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            // Photo upload
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    .clickable { photoPickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (userInfo.photoUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(context)
                                .data(userInfo.photoUri)
                                .build()
                        ),
                        contentDescription = "Profile Photo",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        painter = painterResource(android.R.drawable.ic_menu_camera),
                        contentDescription = "Upload Photo",
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                text = "Upload Photo",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Specialty field
            OutlinedTextField(
                value = userInfo.specialty,
                onValueChange = { userInfo = userInfo.copy(specialty = it) },
                label = { Text("Specialty") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Phone number field
            OutlinedTextField(
                value = userInfo.phoneNumber,
                onValueChange = { userInfo = userInfo.copy(phoneNumber = it) },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Social media section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Social Media",
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Instagram
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            checked = userInfo.hasInstagram,
                            onCheckedChange = {
                                userInfo = userInfo.copy(hasInstagram = it)
                                if (!it) userInfo = userInfo.copy(instagramUrl = "")
                            }
                        )
                        Text(
                            text = "Instagram",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    if (userInfo.hasInstagram) {
                        OutlinedTextField(
                            value = userInfo.instagramUrl,
                            onValueChange = { userInfo = userInfo.copy(instagramUrl = it) },
                            label = { Text("Instagram URL") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Uri,
                                imeAction = ImeAction.Next
                            )
                        )
                    }

                    // LinkedIn
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            checked = userInfo.hasLinkedIn,
                            onCheckedChange = {
                                userInfo = userInfo.copy(hasLinkedIn = it)
                                if (!it) userInfo = userInfo.copy(linkedInUrl = "")
                            }
                        )
                        Text(
                            text = "LinkedIn",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    if (userInfo.hasLinkedIn) {
                        OutlinedTextField(
                            value = userInfo.linkedInUrl,
                            onValueChange = { userInfo = userInfo.copy(linkedInUrl = it) },
                            label = { Text("LinkedIn URL") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Uri,
                                imeAction = ImeAction.Next
                            )
                        )
                    }

                    // Facebook
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            checked = userInfo.hasFacebook,
                            onCheckedChange = {
                                userInfo = userInfo.copy(hasFacebook = it)
                                if (!it) userInfo = userInfo.copy(facebookUrl = "")
                            }
                        )
                        Text(
                            text = "Facebook",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    if (userInfo.hasFacebook) {
                        OutlinedTextField(
                            value = userInfo.facebookUrl,
                            onValueChange = { userInfo = userInfo.copy(facebookUrl = it) },
                            label = { Text("Facebook URL") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Uri,
                                imeAction = ImeAction.Next
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Experience field
            OutlinedTextField(
                value = userInfo.experience,
                onValueChange = { userInfo = userInfo.copy(experience = it) },
                label = { Text("Experience") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Clinic Information",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    OutlinedTextField(
                        value = userInfo.clinic.name,
                        onValueChange = { userInfo = userInfo.copy(clinic = userInfo.clinic.copy(name = it)) },
                        label = { Text("Clinic Name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                    )

                    OutlinedTextField(
                        value = userInfo.clinic.address,
                        onValueChange = { userInfo = userInfo.copy(clinic = userInfo.clinic.copy(address = it)) },
                        label = { Text("Clinic Address") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        minLines = 2,
                        maxLines = 3,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                    )

                    OutlinedTextField(
                        value = userInfo.clinic.mapLocation,
                        onValueChange = { userInfo = userInfo.copy(clinic = userInfo.clinic.copy(mapLocation = it)) },
                        label = { Text("Map Location (Google Maps link or coordinates)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Spacer(modifier = Modifier.height(24.dp))

            // Working Days Schedule section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Working Schedule",
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Work everyday toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = userInfo.workEveryday,
                            onCheckedChange = {
                                userInfo = userInfo.copy(workEveryday = it)
                                if (it) {
                                    // Reset all individual day toggles if "work everyday" is checked
                                    userInfo = userInfo.copy(workingDays = userInfo.workingDays.map { d ->
                                        d.copy(isWorking = false, startTime = "", endTime = "")
                                    })
                                }
                            }
                        )
                        Text(
                            text = "Work everyday?",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    // Working on weekend toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            checked = userInfo.worksOnWeekend,
                            onCheckedChange = { userInfo = userInfo.copy(worksOnWeekend = it) }
                        )
                        Text(
                            text = "Working on weekends",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (userInfo.workEveryday) {
                        // Show single start and end time for all days
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            OutlinedTextField(
                                value = userInfo.startWorkTime,
                                onValueChange = { userInfo = userInfo.copy(startWorkTime = it) },
                                label = { Text("Start Time") },
                                modifier = Modifier.weight(1f).padding(end = 8.dp),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                            )
                            OutlinedTextField(
                                value = userInfo.endWorkTime,
                                onValueChange = { userInfo = userInfo.copy(endWorkTime = it) },
                                label = { Text("End Time") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                            )
                        }
                    } else {
                        // Working days selection with individual time slots
                        userInfo.workingDays.forEachIndexed { index, day ->
                            val updatedWorkingDays = userInfo.workingDays.toMutableList()

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Switch(
                                        checked = day.isWorking,
                                        onCheckedChange = { isChecked ->
                                            updatedWorkingDays[index] = day.copy(isWorking = isChecked)
                                            if (!isChecked) {
                                                updatedWorkingDays[index] = day.copy(
                                                    isWorking = false,
                                                    startTime = "",
                                                    endTime = ""
                                                )
                                            }
                                            userInfo = userInfo.copy(workingDays = updatedWorkingDays)
                                        }
                                    )
                                    Text(
                                        text = day.day,
                                        modifier = Modifier.padding(start = 8.dp),
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                if (day.isWorking) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 48.dp, top = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        OutlinedTextField(
                                            value = day.startTime,
                                            onValueChange = { newTime ->
                                                updatedWorkingDays[index] = day.copy(startTime = newTime)
                                                userInfo = userInfo.copy(workingDays = updatedWorkingDays)
                                            },
                                            label = { Text("Start") },
                                            modifier = Modifier.weight(1f).padding(end = 8.dp),
                                            singleLine = true,
                                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                                        )

                                        OutlinedTextField(
                                            value = day.endTime,
                                            onValueChange = { newTime ->
                                                updatedWorkingDays[index] = day.copy(endTime = newTime)
                                                userInfo = userInfo.copy(workingDays = updatedWorkingDays)
                                            },
                                            label = { Text("End") },
                                            modifier = Modifier.weight(1f),
                                            singleLine = true,
                                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                                        )
                                    }
                                }

                                if (index < userInfo.workingDays.size - 1) {
                                    Divider(
                                        modifier = Modifier.padding(top = 8.dp),
                                        color = MaterialTheme.colorScheme.outlineVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Break Times section (as toggle + start/end time fields)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Break Time",
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedTextField(
                            value = userInfo.breakTime.start,
                            onValueChange = { userInfo = userInfo.copy(breakTime = userInfo.breakTime.copy(start = it)) },
                            label = { Text("Break Start") },
                            modifier = Modifier.weight(1f).padding(end = 8.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                        )
                        OutlinedTextField(
                            value = userInfo.breakTime.end,
                            onValueChange = { userInfo = userInfo.copy(breakTime = userInfo.breakTime.copy(end = it)) },
                            label = { Text("Break End") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                        )
                    }
                    Text(
                        text = "Breaks will be marked as unavailable in your schedule.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Save Information", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

