package com.example.medicall.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.medicall.ui.components.Header
import com.example.medicall.ui.components.DoctorsList


@Composable
fun Home() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Header(userName = "Bouchra")
        Spacer(modifier = Modifier.height(16.dp))
        DoctorsList()

    }
}
