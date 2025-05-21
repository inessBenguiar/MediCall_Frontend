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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

data class NavItem(
    val icon: ImageVector,
    val title: String,
    val route: String
)

@Composable
fun Navbar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    navController: NavController,
    userId: String
) {
    val navItems = listOf(
        NavItem(Icons.Filled.Home, "Accueil", "home"),
        NavItem(Icons.Filled.Favorite, "Favoris", "favoris"),
        NavItem(Icons.Filled.Event, "Calendrier", "calendrier"),
        NavItem(Icons.Filled.Person, "Profil", "profil/$userId")
    )

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
                navItems.forEachIndexed { index, item ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable {
                                onItemSelected(index)
                                navController.navigate(item.route)
                            }
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

@Composable
@Preview(showBackground = true)
fun PreviewNavbar() {
    val navController = rememberNavController()
    var selectedIndex by remember { mutableStateOf(0) }

    // Exemple avec ID fictif
    Navbar(
        selectedIndex = selectedIndex,
        onItemSelected = { selectedIndex = it },
        navController = navController,
        userId = "123"
    )
}
