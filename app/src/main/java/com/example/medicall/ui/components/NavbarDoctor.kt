package com.example.medicall.ui.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class NavItemMedecin(val icon: androidx.compose.ui.graphics.vector.ImageVector, val title: String)

val navItemsMedecin = listOf(
    NavItemMedecin(Icons.Filled.Event, "Rendez-vous"),
    NavItemMedecin(Icons.Filled.QrCodeScanner, "Scan QR"),
    NavItemMedecin(Icons.Filled.Notifications, "Notifications"),
    NavItemMedecin(Icons.Filled.Person, "Profil"),
)

@Composable
fun NavbarDoctor(selectedIndex: Int, onItemSelected: (Int) -> Unit) {
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
                .height(90.dp),
            containerColor = Color.White
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                navItemsMedecin.forEachIndexed { index, item ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable { onItemSelected(index) }
                            .padding(vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = if (index == selectedIndex) Color(0xFF007BFF) else Color.Gray,
                            modifier = Modifier.size(35.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.title,
                            color = if (index == selectedIndex) Color(0xFF007BFF) else Color.Gray,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNavbarMedecin() {
    var selected by remember { mutableStateOf(0) }
    NavbarDoctor(selected) { selected = it }
}
