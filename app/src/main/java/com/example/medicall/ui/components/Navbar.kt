package com.example.app.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.medicall.Navigation.Screens
import com.example.medicall.ui.components.NavigationItem
import com.example.medicall.ui.components.ProfileMenuWithDropdown

data class NavItem(val icon: androidx.compose.ui.graphics.vector.ImageVector, val title: String,val route: String)

val navItems = listOf(
    NavItem(Icons.Filled.Home, "Accueil",Screens.DoctorProfil.route),
    NavItem(Icons.Filled.Favorite, "Favoris",Screens.DoctorProfil.route),
    NavItem(Icons.Filled.Event, "Calendrier",Screens.DoctorProfil.route),
    NavItem(Icons.Filled.Person, "Profil",Screens.Home.route),
)

@Composable
fun Navbar(navController: NavController, selectedIndex: Int, onItemSelected: (Int) -> Unit) {
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
                .height(90.dp), // plus de hauteur
            containerColor = Color.White
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                navItems.forEachIndexed { index, item ->
                    if (index == 3) { // Profile icon (index 3)
                        val navItem = NavigationItem(item.icon, item.title, item.route)
                        ProfileMenuWithDropdown(
                            item = navItem,
                            isSelected = index == selectedIndex,
                            navController = navController,
                            onItemClick = { onItemSelected(index) }
                        )
                    } else {
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
                                modifier = Modifier.size(35.dp) // Icônes plus grandes
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
}

/*@Preview(showBackground = true)
@Composable
fun PreviewNavbar() {
    var selected by remember { mutableStateOf(0) }
    Navbar(selected) { selected = it }
}
*/