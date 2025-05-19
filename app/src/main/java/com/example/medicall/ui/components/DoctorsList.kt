package com.example.medicall.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.medicall.viewmodel.DoctorModel
import com.example.medicall.util.makeToast
import com.example.errorMessage
import com.example.medicall.Navigation.Screens

@Composable
fun DoctorsList(doctorModel: DoctorModel, navController: NavController) {
    val context = LocalContext.current
    val data = doctorModel.doctors.value
    val loading = doctorModel.loading.value
    val error = doctorModel.error.value

    var selectedSpecialty by remember { mutableStateOf("All") }
    val specialties = listOf("All") + data.map { it.specialty }.distinct()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 0.dp) // Reduce vertical padding
    ) {
        SpecialtyFilter(
            specialties = specialties,
            selectedSpecialty = selectedSpecialty,
            onSpecialtySelected = { selectedSpecialty = it }
        )

        Spacer(modifier = Modifier.height(0.dp)) // Force no space

        if (loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            val filteredDoctors = if (selectedSpecialty == "All") data else data.filter {
                it.specialty == selectedSpecialty
            }

            // More aggressive adjustments to LazyColumn
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(0.dp), // Zero padding all around
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 0.dp), // Explicitly set top padding to zero
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(filteredDoctors) { doctor ->
                    DoctorCard(
                        first_name = doctor.first_name,
                        family_name = doctor.family_name,
                        specialty = doctor.specialty,
                        address = doctor.clinic,
                        phone = doctor.contact,
                        photoUrl = doctor.photo,
                        onClick = {
                            navController.navigate(
                                Screens.DoctorDetailScreen.route +
                                        "?firstName=${doctor.first_name}" +
                                        "&familyName=${doctor.family_name}" +
                                        "&photoUrl=${doctor.photo}" +
                                        "&address=${doctor.clinic}" +
                                        "&phone=${doctor.contact}"
                            )
                        }
                    )
                }

            }
        }

        if (error) {
            errorMessage.makeToast(context)
            doctorModel.error.value = false
        }
    }
}

@Composable
fun SpecialtyFilter(
    specialties: List<String>,
    selectedSpecialty: String,
    onSpecialtySelected: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        specialties.forEach { specialty ->
            Button(
                onClick = { onSpecialtySelected(specialty) },
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (specialty == selectedSpecialty) Color(0xFF1676F3) else Color(0xFFE8F4FF),
                    contentColor = if (specialty == selectedSpecialty) Color.White else Color(0xFF1676F3)
                )
            ) {
                Text(text = specialty, fontSize = 14.sp)
            }
        }
    }
}
@Composable
fun DoctorCard(
    first_name: String,
    family_name:String,
    specialty: String,
    address: String,
    phone: String,
    photoUrl: String,
    onClick: () -> Unit

) {
    var isFavorite by remember { mutableStateOf(false) }
    val rating = remember { (1..5).random() }

    Card(
        onClick = onClick,

        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.width(320.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(end = 16.dp)
            ) {
                AsyncImage(
                    model = photoUrl,
                    contentDescription = family_name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(60.dp)
                        .background(Color.LightGray, RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    for (i in 1..5) {
                        Icon(
                            imageVector = if (i <= rating) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = "Star $i",
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text("$first_name $family_name", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                Text(specialty, fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = address,
                    fontSize = 12.sp,
                    color = Color.Black,
                    textDecoration = TextDecoration.Underline
                )

                Spacer(modifier = Modifier.height(3.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Phone",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(16.dp)
                    )
                    SelectionContainer {
                        Text(
                            text = phone,
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }

            IconButton(
                onClick = { isFavorite = !isFavorite },
                modifier = Modifier.size(36.dp),
                content = {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )
        }
    }
}