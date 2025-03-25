package com.example.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.draw.clip

data class NavItem(val icon: androidx.compose.ui.graphics.vector.ImageVector, val title: String)

val navItems = listOf(
    NavItem(Icons.Filled.Home, "Accueil"),
    NavItem(Icons.Filled.Favorite, "Favoris"),
    NavItem(Icons.Filled.CalendarToday, "Rendez-vous"),
    NavItem(Icons.Filled.Person, "Profil")

)

@Composable
fun Navbar(selectedIndex: Int, onItemSelected: (Int) -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        BottomAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            containerColor = Color.White
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                navItems.forEachIndexed { index, item ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (index == selectedIndex) Color(0xFF007BFF) else Color.Transparent) // Bleu actif
                            .padding(6.dp)
                            .clickable { onItemSelected(index) }
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = if (index == selectedIndex) Color.White else Color.Gray, // Icône active blanche
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}
@Preview
@Composable
fun PreviewNavbar() {
    var selected by remember { mutableStateOf(0) }
    Navbar(selected) { selected = it }
}
