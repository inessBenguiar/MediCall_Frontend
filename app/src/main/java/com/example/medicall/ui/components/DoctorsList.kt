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
import com.example.medicall.R // Assurez-vous que vos images sont dans res/drawable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star

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
    Doctor("Dr. Pediatrician", "Specialist Cardiologist", "HeartCare Clinic, NY", "(555) 123-4567", 5.0, R.drawable.doctor1),
    Doctor("Dr. Mistry Brick", "Specialist Dentist", "HeartCare Clinic, NY", "(555) 123-4567", 4.8, R.drawable.doctor2),
    Doctor("Dr. Ether Wall", "Specialist Cancer", "HeartCare Clinic, NY", "(555) 123-4567", 4.5, R.drawable.doctor3)
)

@Composable
fun DoctorsList() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(doctors) { doctor ->
            DoctorCard(doctor)
        }
    }
}

@Composable
fun DoctorCard(doctor: Doctor) {
    var isFavorite by remember { mutableStateOf(false) }

    Card(
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
            // Image du médecin avec une étoile en dessous
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = doctor.imageRes),
                    contentDescription = doctor.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Évaluation du médecin sous l’image
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

            // Infos du médecin
            Column(modifier = Modifier.weight(1f)) {
                Text(doctor.name, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
                Text(doctor.specialty, fontSize = 14.sp, color = Color.Gray)

                Spacer(modifier = Modifier.height(4.dp))

                // Adresse soulignée
                Text(
                    text = doctor.address,
                    fontSize = 12.sp,
                    color = Color.Black,
                    textDecoration = TextDecoration.Underline
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Numéro de téléphone avec icône
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

            // Bouton Favoris
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

@Composable
fun PreviewDoctorsList() {
    DoctorsList()
}
