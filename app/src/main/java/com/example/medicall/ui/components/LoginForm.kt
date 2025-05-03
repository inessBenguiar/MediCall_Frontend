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
import androidx.navigation.NavController
import com.example.medicall.R
import com.example.medicall.service.AuthService
import com.example.medicall.service.LoginRequest
import com.example.medicall.service.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun LoginForm(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }


    val context = LocalContext.current
    val authService = AuthService.createInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Log in to your Account", fontSize = 20.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        Text("Welcome back, please enter your details.", fontSize = 14.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { /* Connexion Google */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color.LightGray),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.google),
                contentDescription = "Google",
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text("Continue with Google", color = Color.Black)
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(image, contentDescription = "Toggle Password Visibility")
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp, max = 56.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = rememberMe,
                onCheckedChange = { rememberMe = it }
            )
            Text("Remember me", modifier = Modifier.clickable { rememberMe = !rememberMe })
            Spacer(modifier = Modifier.weight(1f))
            Text("Forgot Password?", color = Color(0xFF1676F3), modifier = Modifier.clickable { /* Action */ })
        }

        Spacer(modifier = Modifier.height(16.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                // Validation: Vérifier si l'email et le mot de passe sont remplis
                if (email.isBlank() || password.isBlank()) {
                    // Afficher un Toast si l'email ou le mot de passe est vide
                    Toast.makeText(context, "Please fill in both fields", Toast.LENGTH_SHORT).show()
                } else {
                    // Créer la requête de connexion
                    val request = LoginRequest(email = email, password = password)

                    // Faire la requête de connexion
                    authService.login(request).enqueue(object : Callback<LoginResponse> {
                        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                            if (response.isSuccessful) {
                                // Vérifier si l'access_token est présent et valide
                                val accessToken = response.body()?.access_token
                                if (!accessToken.isNullOrBlank()) {
                                    // Si l'access_token est valide, rediriger vers la page "home"
                                    navController.navigate("home")
                                } else {
                                    // Si l'access_token est vide ou absent, afficher un message d'erreur
                                    Toast.makeText(context, "Invalid credentials. Please try again.", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                // Si la réponse est échouée, afficher un message d'erreur
                                Toast.makeText(context, "Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                            // Si la requête échoue (problème réseau, serveur, etc.), afficher un message d'erreur
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
            Text("Log in", color = Color.White)
        }


        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Text("Don’t have an account?", color = Color.Gray)
            Spacer(modifier = Modifier.width(4.dp))
            Text("Sign Up", color = Color(0xFF1676F3), modifier = Modifier.clickable {
                navController.navigate("signup")
            })
        }
    }

}
