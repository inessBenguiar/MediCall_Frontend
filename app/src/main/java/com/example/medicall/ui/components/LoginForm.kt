package com.example.medicall.ui.components
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
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.medicall.R
import com.example.medicall.errorMessage
import com.example.medicall.ui.Navigation.Screens
import com.example.medicall.ui.preferences.saveId
import com.example.medicall.ui.viewModel.AuthModel

/*class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginForm()
        }
    }
}*/

@Composable
fun LoginForm(authModel: AuthModel, navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }
    var loginClicked by remember { mutableStateOf(false) }

    val loginMap = remember { mutableStateOf<Map<String, String>>(emptyMap()) }

    var role: String
    val context = LocalContext.current
    val data = authModel.role.value
    val loading = authModel.loading.value
    val error = authModel.error.value

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    if (loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()

        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .height(screenHeight - 130.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.logo), // Remplacez par votre logo
            contentDescription = "Logo",
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Log in to your Account", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text("Welcome back, please enter your details.", fontSize = 14.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { saveId(context, password, email ) },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color.LightGray),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Icon(
                painterResource(id = R.drawable.google),
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
            singleLine = true,
            label = { Text("Email Address") },
            modifier = Modifier
                .fillMaxWidth()

        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            singleLine = true,
            isError = password.isNotEmpty(),
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
        if(rememberMe){
            saveId(context,email, password)}
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                loginMap.value = mapOf(
                    "email" to email.trim(),
                    "password" to password)
                navController.navigate(Screens.Home.route){popUpTo(0)} },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF1676F3)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Log in", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Text("Don’t have an account?", color = Color.Gray)
            Spacer(modifier = Modifier.width(4.dp))
            Text("Sign Up", color = Color(0xFF1676F3), modifier = Modifier.clickable { navController.navigate(Screens.MainScreen.route) })
        }
    }
    if(error) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        authModel.error.value = false
    }
    LaunchedEffect(loginClicked) {
        role = authModel.authentication(loginMap).toString()
        loginClicked = false // Reset the trigger

    }
}
