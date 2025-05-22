import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TimeSlot(
    val slot: Int,
    val time: String
)
@JsonClass(generateAdapter = true)
data class BookResponse(
    val status: String,
    val message: String
)

@JsonClass(generateAdapter = true)
data class BookAppointmentRequest(
    val doctorId: Int,
    val patientId: Int,
    val date: String,  // Format: YYYY-MM-DD
    val slotPosition: Int
)

@JsonClass(generateAdapter = true)
data class Appointment(
    val id: Int,
    val doctorId: Int,
    val patientId: Int,
    val date: String,  // Format: YYYY-MM-DD
    val slotPosition: Int,
    val status: String,
    val createdAt: String,
    val updatedAt: String
)



