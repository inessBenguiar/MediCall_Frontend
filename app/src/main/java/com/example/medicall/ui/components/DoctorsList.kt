package com.example.medicall.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medicall.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.medicall.ui.Navigation.Screens

// Modèle de données pour un médecin
data class Doctor(
    val name: String,
    val specialty: String,
    val address: String,
    val phone: String,
    val rating: Double,
    val imageRes: Int
)

// Liste des médecins
val doctors = listOf(
    Doctor("Dr. Pediatrician", "Cardiology", "HeartCare Clinic, NY", "(555) 123-4567", 5.0, R.drawable.doctor1),
    Doctor("Dr. Mistry Brick", "Dentist", "DentalCare Clinic, NY", "(555) 987-6543", 4.8, R.drawable.doctor2),
    Doctor("Dr. Ether Wall", "Cancer", "Oncology Center, NY", "(555) 246-1357", 4.5, R.drawable.doctor3)
)

// Liste des spécialités disponibles
val specialties = listOf("All", "Cardiology", "Dentist")

@Composable
fun DoctorsList(navController: NavController) {
    var selectedSpecialty by remember { mutableStateOf("All") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Filtres par spécialité
        SpecialtyFilter(
            specialties = specialties,
            selectedSpecialty = selectedSpecialty,
            onSpecialtySelected = { selectedSpecialty = it }
        )

        // Liste filtrée des médecins
        val filteredDoctors = if (selectedSpecialty == "All") doctors
        else doctors.filter { it.specialty == selectedSpecialty }

        DoctorsList(filteredDoctors,navController)
    }
}

@Composable
fun SpecialtyFilter(
    specialties: List<String>,
    selectedSpecialty: String,
    onSpecialtySelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        specialties.forEach { specialty ->
            Button(
                onClick = { onSpecialtySelected(specialty) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedSpecialty == specialty) Color(0xFF1676F3) else Color(0xFFE8F4FF),
                    contentColor = if (selectedSpecialty == specialty) Color.White else Color(0xFF1676F3)

                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = specialty, fontSize = 14.sp)
            }
        }
    }
    Spacer(modifier = Modifier.height(12.dp))

}

@Composable
fun DoctorsList(doctors: List<Doctor>,navController: NavController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(doctors) { doctor ->
            DoctorCard(doctor,navController)
        }

    }

}


@Composable
fun DoctorCard(doctor: Doctor,navController: NavController) {
    var isFavorite by remember { mutableStateOf(false) }

    Card( onClick = {navController.navigate(Screens.DoctorInfo.route)},
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .width(320.dp)
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = doctor.imageRes),
                    contentDescription = doctor.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(55.dp)
                        .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = doctor.rating.toString(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(doctor.name, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
                Text(doctor.specialty, fontSize = 14.sp, color = Color.Gray)

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = doctor.address,
                    fontSize = 12.sp,
                    color = Color.Black,
                    textDecoration = TextDecoration.Underline
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Phone",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(16.dp)
                    )
                    SelectionContainer {
                        Text(
                            text = doctor.phone,
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }

            IconButton(onClick = { isFavorite = !isFavorite }) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) Color.Red else Color.Gray
                )
            }
        }
    }
}

/*@Preview(showBackground = true)
@Composable
fun PreviewDoctorsScreen() {
    DoctorsList()
}*/
