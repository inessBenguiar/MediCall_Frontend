package com.example.medicall.ui.components


import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.medicall.ui.Navigation.Screens
import com.example.medicall.ui.preferences.deleteId

data class NavItemMedecin(val icon: androidx.compose.ui.graphics.vector.ImageVector, val title: String, val route: String)

val navItemsMedecin = listOf(
    NavItemMedecin(Icons.Filled.Event, "Rendez-vous", Screens.DoctorProfil.route),
    NavItemMedecin(Icons.Filled.QrCodeScanner, "Scan QR", Screens.DoctorProfil.route),
    NavItemMedecin(Icons.Filled.Notifications, "Notifications", Screens.DoctorProfil.route),
    NavItemMedecin(Icons.Filled.Person, "Profil", Screens.DoctorProfil.route),
)

@Composable
fun NavbarDoctor(navController: NavController, selectedIndex: Int, onItemSelected: (Int) -> Unit) {
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
                    if (index == 3) { // Profile icon (index 3)
                        ProfileMenuWithDropdown(
                            item = item,
                            isSelected = index == selectedIndex,
                            navController = navController,
                            onItemClick = { onItemSelected(index) }
                        )
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable {
                                    onItemSelected(index)
                                    navController.navigate(navItemsMedecin[index].route)
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
}
    @Composable
    fun ProfileMenuWithDropdown(
        item: NavItemMedecin,
        isSelected: Boolean,
        navController: NavController,
        onItemClick: () -> Unit
    ) {
        var showDropdownMenu by remember { mutableStateOf(false) }
        val context = LocalContext.current

        Box {
            // Profile Icon that triggers the dropdown
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable {
                        // Toggle dropdown visibility when clicking the profile icon
                        showDropdownMenu = true
                        onItemClick()
                    }
                    .padding(vertical = 8.dp)
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = if (isSelected) Color(0xFF007BFF) else Color.Gray,
                    modifier = Modifier.size(35.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.title,
                    color = if (isSelected) Color(0xFF007BFF) else Color.Gray,
                    style = MaterialTheme.typography.labelMedium
                )
            }

            // Dropdown menu
            DropdownMenu(
                expanded = showDropdownMenu,
                onDismissRequest = { showDropdownMenu = false },
                modifier = Modifier
                    .width(200.dp)
                    .background(Color.White)
            ) {
                // Profile menu item
                DropdownMenuItem(
                    text = { Text("Mon profil") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Profil",
                            tint = Color(0xFF007BFF)
                        )
                    },
                    onClick = {
                        navController.navigate(Screens.DoctorProfil.route)
                        showDropdownMenu = false
                    }
                )

                // Add a divider
                Divider()

                // Logout menu item
                DropdownMenuItem(
                    text = { Text("Déconnexion") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.ExitToApp,
                            contentDescription = "Déconnexion",
                            tint = Color.Red
                        )
                    },
                    onClick = {
                        // Handle logout action
                        deleteId(context)
                        navController.navigate(com.example.medicall.Navigation.Screens.MainScreen.route)
                        showDropdownMenu = false
                    }
                )
            }
        }
    }

/*@Preview(showBackground = true)
@Composable
fun PreviewNavbarMedecin() {
    var selected by remember { mutableStateOf(0) }
    NavbarDoctor(selected) { selected = it }
}*/
