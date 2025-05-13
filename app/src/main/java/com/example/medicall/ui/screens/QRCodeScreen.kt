import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import com.example.medicall.util.QRCodeUtils

@Composable
fun QRCodeScreen(appointmentId: String) {
    val qrBitmap = QRCodeUtils.generateQRCode(appointmentId)
    Image(bitmap = qrBitmap.asImageBitmap(), contentDescription = "Appointment QR Code")
}
