package com.example.medicall
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val authService = AuthService.createInstance()
    val scrollState = rememberScrollState()

    // Couleurs personnalisées
    val primaryBlue = Color(0xFF1676F3)
    val lightBlue = Color(0xFFE3F2FD)
    val darkGray = Color(0xFF2C2C2C)
    val lightGray = Color(0xFF9E9E9E)
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFF8FAFE),
            Color(0xFFFFFFFF)
        )
    )

    val roleOptions = listOf("patient", "doctor")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(20.dp),
                    ambientColor = primaryBlue.copy(alpha = 0.1f),
                    spotColor = primaryBlue.copy(alpha = 0.1f)
                ),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo avec cercle coloré en arrière-plan
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    primaryBlue.copy(alpha = 0.2f),
                                    primaryBlue.copy(alpha = 0.05f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = "Sign Up",
                        modifier = Modifier.size(40.dp),
                        tint = primaryBlue
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Titre avec style amélioré
                Text(
                    text = "Create Account",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = darkGray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Sign up to get started with MediCall",
                    fontSize = 16.sp,
                    color = lightGray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Champs d'inscription avec icônes
                ModernTextField(
                    label = "First Name",
                    value = firstName,
                    onValueChange = { firstName = it },
                    placeholder = "Enter your first name",
                    leadingIcon = Icons.Default.Person
                )

                Spacer(modifier = Modifier.height(16.dp))

                ModernTextField(
                    label = "Last Name",
                    value = lastName,
                    onValueChange = { lastName = it },
                    placeholder = "Enter your last name",
                    leadingIcon = Icons.Default.Person
                )

                Spacer(modifier = Modifier.height(16.dp))

                ModernTextField(
                    label = "Phone Number",
                    value = phone,
                    onValueChange = { phone = it },
                    placeholder = "Enter your phone number",
                    leadingIcon = Icons.Default.Phone,
                    keyboardType = KeyboardType.Phone
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Sélection de rôle avec radio buttons
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Role",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = darkGray,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA)),
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            roleOptions.forEach { option ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { role = option }
                                        .padding(vertical = 8.dp)
                                ) {
                                    RadioButton(
                                        selected = role == option,
                                        onClick = { role = option },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = primaryBlue,
                                            unselectedColor = lightGray
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Icon(
                                        imageVector = if (option == "patient") Icons.Default.Person else Icons.Default.LocalHospital,
                                        contentDescription = null,
                                        tint = if (role == option) primaryBlue else lightGray,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = option.replaceFirstChar { it.uppercase() },
                                        fontSize = 16.sp,
                                        fontWeight = if (role == option) FontWeight.Medium else FontWeight.Normal,
                                        color = if (role == option) primaryBlue else darkGray
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                ModernTextField(
                    label = "Email Address",
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "Enter your email",
                    leadingIcon = Icons.Default.Email,
                    keyboardType = KeyboardType.Email
                )

                Spacer(modifier = Modifier.height(16.dp))

                ModernTextField(
                    label = "Password",
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "Enter your password",
                    leadingIcon = Icons.Default.Lock,
                    isPassword = true,
                    isPasswordVisible = passwordVisible,
                    onPasswordVisibilityChange = { passwordVisible = it }
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Bouton d'inscription avec dégradé
                Button(
                    onClick = {
                        if (firstName.isBlank() || lastName.isBlank() || phone.isBlank() ||
                            role.isBlank() || email.isBlank() || password.isBlank()) {
                            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                        } else {
                            val request = SignupRequest(
                                firstName = firstName,
                                lastName = lastName,
                                phone = phone,
                                role = role,
                                email = email,
                                password = password
                            )

                            authService.signup(request).enqueue(object : Callback<SignupResponse> {
                                override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                                    if (response.isSuccessful) {
                                        Toast.makeText(context, "Account created successfully", Toast.LENGTH_SHORT).show()
                                        navController.navigate("login")
                                    } else {
                                        Toast.makeText(context, "Signup failed. Please try again.", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                                    Toast.makeText(context, "Network error. Please try again later.", Toast.LENGTH_SHORT).show()
                                }
                            })
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryBlue
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(12.dp),
                            ambientColor = primaryBlue.copy(alpha = 0.3f),
                            spotColor = primaryBlue.copy(alpha = 0.3f)
                        )
                ) {
                    Text(
                        "Create Account",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Lien de connexion avec style amélioré
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Already have an account? ",
                        color = lightGray,
                        fontSize = 14.sp
                    )
                    Text(
                        "Sign In",
                        color = primaryBlue,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable {
                            navController.navigate("login")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ModernTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    leadingIcon: ImageVector? = null,
    isPassword: Boolean = false,
    isPasswordVisible: Boolean = false,
    onPasswordVisibilityChange: ((Boolean) -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    val primaryBlue = Color(0xFF1676F3)
    val lightGray = Color(0xFF9E9E9E)
    val darkGray = Color(0xFF2C2C2C)

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = darkGray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    placeholder,
                    color = lightGray,
                    fontSize = 16.sp
                )
            },
            leadingIcon = leadingIcon?.let { icon ->
                {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (value.isNotEmpty()) primaryBlue else lightGray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            trailingIcon = if (isPassword) {
                {
                    val image = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { onPasswordVisibilityChange?.invoke(!isPasswordVisible) }) {
                        Icon(
                            image,
                            contentDescription = "Toggle Password Visibility",
                            tint = lightGray
                        )
                    }
                }
            } else null,
            visualTransformation = if (isPassword && !isPasswordVisible)
                PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = primaryBlue,
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedLabelColor = primaryBlue,
                cursorColor = primaryBlue
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        )
    }
}