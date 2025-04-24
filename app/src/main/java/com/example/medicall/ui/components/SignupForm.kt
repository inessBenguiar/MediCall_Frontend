package com.example.medicall.ui.components
/*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.medicall.R

@Composable
fun SignupForm() {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var termsAccepted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.size(75.dp)
        )

        Text("Create Account", fontSize = 18.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, modifier = Modifier.padding(vertical = 8.dp))
        Text("Sign up to get started", fontSize = 12.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { /* Connexion Google */ },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color.LightGray),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(painterResource(id = R.drawable.google), contentDescription = "Google", tint = Color.Unspecified)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Sign up with Google", color = Color.Black)
        }

        Spacer(modifier = Modifier.height(16.dp))

        LabelledTextField(label = "Full Name", value = fullName, onValueChange = { fullName = it }, placeholder = "Enter your full name")
        LabelledTextField(label = "Email Address", value = email, onValueChange = { email = it }, placeholder = "Enter your email")
        LabelledTextField(label = "Password", value = password, onValueChange = { password = it }, isPassword = true, isVisible = passwordVisible, onVisibilityChange = { passwordVisible = it }, placeholder = "Enter your password")
        LabelledTextField(label = "Confirm Password", value = confirmPassword, onValueChange = { confirmPassword = it }, isPassword = true, isVisible = confirmPasswordVisible, onVisibilityChange = { confirmPasswordVisible = it }, placeholder = "Confirm your password")

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
            Checkbox(checked = termsAccepted, onCheckedChange = { termsAccepted = it })
            Text("I agree to the ", color = Color.Gray)
            Text("Terms of Service", color = Color(0xFF1676F3), modifier = Modifier.clickable { /* Action */ })
        }

        Button(
            onClick = { /* Inscription */ },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF1676F3)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text("Get Started", color = Color.White)
        }

        Row(modifier = Modifier.padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("Already have an account?", color = Color.Gray)
            Spacer(modifier = Modifier.width(4.dp))
            Text("Log in", color = Color(0xFF1676F3), modifier = Modifier.clickable { /* Redirection */ })
        }
    }
}

@Composable
fun LabelledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    isVisible: Boolean = false,
    onVisibilityChange: ((Boolean) -> Unit)? = null,
    placeholder: String = ""
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            visualTransformation = if (isPassword && !isVisible) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = if (isPassword) KeyboardOptions(keyboardType = KeyboardType.Password) else KeyboardOptions.Default,
            trailingIcon = if (isPassword) {
                {
                    val icon = if (isVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { onVisibilityChange?.invoke(!isVisible) }) {
                        Icon(icon, contentDescription = "Toggle Password Visibility")
                    }
                }
            } else null,
            placeholder = { Text(text = label, fontSize = 12.sp, color = Color.Gray) }, // Réduction de la taille
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 14.sp), // Taille du texte saisie
            modifier = Modifier.fillMaxWidth().height(48.dp)
        )

    }
    Spacer(modifier = Modifier.height(8.dp))
}
*/