package com.example.medicall.ui.components

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.example.medicall.R
import com.example.medicall.ui.Navigation.Screens

data class DoctorDetails(
    val id: Int,
    val name: String,
    val profilePicture: Int,
    val specialty: String,
    val rating: Float,
    val workplaceName: String,
    val address: String,  // Simple string address
    val email: String,
    val phoneNumber: String,
    val facebook:String?,
    val instagram:String?,
    val linkedin:String?,
    val workonweekend:Boolean,
    val availability: DoctorAvailability = DoctorAvailability()
)

/*@Preview
@Composable
fun DoctorDetailsPreview (){
    DoctorDetail()
}*/
val doctor = DoctorDetails(1,"Dr. Kapoor",R.drawable.doctor1, "Cardiology", 5.0F,"HeartCare Clinic, NY", "78 Elm St, Chicago, IL","dr.Kapoor@email.com","(555) 123-4567","facebook.com","facebook.com","facebook.com",false)

@Composable
fun DoctorDetail (navController: NavController){
     val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .height(screenHeight)
            .padding(16.dp),
    ) {
        Row (horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()){
            Icon(
            imageVector = Icons.Default.ArrowBackIosNew,
            contentDescription = "Go back Arrow",
            modifier = Modifier
                .requiredSize(size = 30.dp)
            )
            Text(
                text = doctor.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp), // Add some spacing
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.size(30.dp))
        }
        Spacer(modifier = Modifier.height(5.dp))
        Column(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {


            Image(
                painter = painterResource(doctor.profilePicture), // Your image in res/drawable
                contentDescription = "Doctor profile picture",
                modifier = Modifier
                    .size(100.dp).align(Alignment.CenterHorizontally)
                    .clip(RoundedCornerShape(15.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = doctor.specialty,
                style = MaterialTheme.typography.titleMedium,
                color = Color.Gray,
                fontSize = 20.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()) {

                Icon(
                    Icons.Default.Star,
                    contentDescription = "Rating",
                    tint = Color(0xFFF89603),
                    modifier = Modifier.size(20.dp)
                )

                Text(
                    text = "%.1f".format(doctor.rating),
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 15.sp,
                )
            }

        }

        Spacer(modifier = Modifier.height(5.dp))

        // Workplace Section
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "WorkPlace:",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold ,
                fontSize = 22.sp,
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = doctor.workplaceName,
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF1676F3),
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
            )
            Row(
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.mapicon),
                    contentDescription = "Map",
                    modifier = Modifier.size(25.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.width(15.dp))
                Text(
                    text = doctor.address,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp,
                    textDecoration = TextDecoration.Underline,
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "Contact:",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Icon(
                    Icons.Default.Phone,
                    contentDescription = "Phone",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = doctor.phoneNumber,
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 22.sp,

                    )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Icon(
                    Icons.Default.Email,
                    contentDescription = "Email",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = doctor.email,
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 22.sp,
                )
            }
        }


            Divider(
                thickness = 2.dp,
                color = Color.Gray.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth().padding(top = 15.dp)
            )
             Spacer(modifier = Modifier.height(17.dp))

            SocialMedia()

            Spacer(modifier = Modifier.height(20.dp))

        Button( onClick = {navController.navigate(Screens.Booking.route)},

            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF007AFF), // Set the background color here
                contentColor = Color.White // You can set the content (text) color
            ),
                modifier = Modifier.fillMaxWidth().height(50.dp).clip(
                    RoundedCornerShape(10.dp)
                )
            ) {

                Text("Book an Appointment", color= Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            }
    }
}

@Composable
fun SocialMedia(){
    val context = LocalContext.current

    Row (horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(0.7f))        {
        // Facebook Icon
        doctor.facebook?.let { url ->
            IconButton(
                onClick = {
                    openSocialMedia(url, context)
                },
                modifier = Modifier.size(40.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.facebookicon),
                    contentDescription = "Facebook",
                    contentScale = ContentScale.Fit,

                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        // Instagram Icon
        doctor.instagram?.let { url ->
            IconButton(
                onClick = {
                    openSocialMedia(url, context)
                },
                modifier = Modifier.size(40.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.instagramicon), // You need to add this vector asset
                    contentDescription = "Instagram",
                    contentScale = ContentScale.Fit
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        // LinkedIn Icon
        doctor.linkedin?.let { url ->
            IconButton(
                onClick = {
                    openSocialMedia(url, context)
                },
                modifier = Modifier.size(40.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.linkedinicon), // You need to add this vector asset
                    contentDescription = "LinkedIn",
                    contentScale = ContentScale.Crop

                )
            }
        }
    }
}

private fun openSocialMedia(url: String, context: Context) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        context.startActivity(intent)
    } catch (e: Exception) {
        // Handle case where no browser is available
        Toast.makeText(context, "Cannot open link", Toast.LENGTH_SHORT).show()
    }

}