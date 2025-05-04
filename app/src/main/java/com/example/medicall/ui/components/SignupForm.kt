package com.example.medicall

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
<<<<<<< HEAD
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.medicall.R
import com.example.medicall.ui.Navigation.Screens
import com.example.medicall.ui.preferences.saveId

@Composable
fun SignupForm(navController: NavController) {

    var fullName by remember { mutableStateOf("") }
=======
import androidx.navigation.NavController
import com.example.medicall.service.AuthService
import com.example.medicall.service.SignupRequest
import com.example.medicall.service.SignupResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun SignupForm(navController: NavController) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
>>>>>>> 009dfc0e47a21c49ad17618605efd5d4e175b6c2
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val authService = AuthService.createInstance()

    Column(
        modifier = Modifier
<<<<<<< HEAD
            .fillMaxSize().fillMaxHeight()
            .padding(24.dp),
=======
            .fillMaxSize()
            .padding(12.dp),
>>>>>>> 009dfc0e47a21c49ad17618605efd5d4e175b6c2
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

     /*   Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.size(100.dp)
        )*/

        Spacer(modifier = Modifier.height(16.dp))

        Text("Create Account", fontSize = 20.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        Text("Sign up to get started", fontSize = 14.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(24.dp))

        // Add your input fields here
        LabelledTextField(label = "First Name", value = firstName, onValueChange = { firstName = it }, placeholder = "Enter your first name")
        LabelledTextField(label = "Last Name", value = lastName, onValueChange = { lastName = it }, placeholder = "Enter your last name")
        LabelledTextField(label = "Phone Number", value = phone, onValueChange = { phone = it }, placeholder = "Enter your phone number")
        LabelledTextField(label = "Role", value = role, onValueChange = { role = it }, placeholder = "Enter your role (patient or doctor)")
        LabelledTextField(label = "Email", value = email, onValueChange = { email = it }, placeholder = "Enter your email")
        LabelledTextField(
            label = "Password",
            value = password,
            onValueChange = { password = it },
            isPassword = true,
            isVisible = passwordVisible,
            onVisibilityChange = { passwordVisible = it },
            placeholder = "Enter your password"
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                // Validation: Ensure all fields are filled
                if (firstName.isBlank() || lastName.isBlank() || phone.isBlank() || role.isBlank() || email.isBlank() || password.isBlank()) {
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                } else {
                    // Create the signup request
                    val request = SignupRequest(
                        firstName = firstName,
                        lastName = lastName,
                        phone = phone,
                        role = role,
                        email = email,
                        password = password
                    )

                    // Perform the signup request
                    authService.signup(request).enqueue(object : Callback<SignupResponse> {
                        override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                            if (response.isSuccessful) {
                                // If successful, navigate to home or dashboard
                                Toast.makeText(context, "Account created successfully", Toast.LENGTH_SHORT).show()
                                navController.navigate("home")
                            } else {
                                // If error occurred, show error message
                                Toast.makeText(context, "Signup failed. Please try again.", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                            // Network failure
                            Toast.makeText(context, "Network error. Please try again later.", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1676F3)
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Up", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

<<<<<<< HEAD
        LabelledTextField(label = "Full Name", value = fullName, onValueChange = { fullName = it }, placeholder = "Enter your full name")
        LabelledTextField(label = "Email Address", value = email, onValueChange = { email = it }, placeholder = "Enter your email")


        //LabelledTextField(label = "Password", value = password, onValueChange = { password = it }, isPassword = true, isVisible = passwordVisible, onVisibilityChange = { passwordVisible = it }, placeholder = "Enter your password")
        //LabelledTextField(label = "Confirm Password", value = confirmPassword, onValueChange = { confirmPassword = it }, isPassword = true, isVisible = confirmPasswordVisible, onVisibilityChange = { confirmPasswordVisible = it }, placeholder = "Confirm your password")

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
            Checkbox(checked = termsAccepted, onCheckedChange = { termsAccepted = it })
            Text("I agree to the ", color = Color.Gray)
            Text("Terms of Service", color = Color(0xFF1676F3), modifier = Modifier.clickable { /* Action */ })
        }
        val context = LocalContext.current
        Button(
            onClick = {
                navController.navigate(Screens.Home.route)
                saveId(context,password,email)},
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF1676F3)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text("Get Started", color = Color.White)
        }

        Row(modifier = Modifier.padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("Already have an account?", color = Color.Gray)
            Spacer(modifier = Modifier.width(4.dp))
            Text("Log in", color = Color(0xFF1676F3), modifier = Modifier.clickable { navController.navigate(Screens.MainScreen.route) })
=======
        Row {
            Text("Already have an account?", color = Color.Gray)
            Spacer(modifier = Modifier.width(4.dp))
            Text("Log In", color = Color(0xFF1676F3), modifier = Modifier.clickable {
                navController.navigate("login")
            })
>>>>>>> 009dfc0e47a21c49ad17618605efd5d4e175b6c2
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
            placeholder = { Text(text = placeholder, fontSize = 12.sp, color = Color.Gray) }, // Réduction de la taille
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 14.sp), // Taille du texte saisie
            modifier = Modifier.fillMaxWidth().height(48.dp)
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}
