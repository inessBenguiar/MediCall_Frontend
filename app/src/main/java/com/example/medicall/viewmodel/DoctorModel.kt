package com.example.medicall.viewmodel



import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medicall.entity.ClinicResponse
import com.example.medicall.entity.Doctor
import com.example.medicall.repository.DoctorRepository
import com.example.medicall.service.ApiResult
import com.example.medicall.ui.components.UserInfo
import com.example.medicall.ui.components.WorkingDay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DoctorModel(private val repository: DoctorRepository, private val context : Context): ViewModel() {
    val doctors = mutableStateOf(emptyList<Doctor>())
    val loading = mutableStateOf(false)
    val error = mutableStateOf(false)
    val errorMessage = mutableStateOf("Error: Try again please")
    val doctor = mutableStateOf<Doctor?>(null)


    //Profile States
    // States
    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Initial)
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    private val _workingDaysState = MutableStateFlow<WorkingDaysState>(WorkingDaysState.Initial)
    val workingDaysState: StateFlow<WorkingDaysState> = _workingDaysState.asStateFlow()

    private val _photoUploadState = MutableStateFlow<PhotoUploadState>(PhotoUploadState.Initial)
    val photoUploadState: StateFlow<PhotoUploadState> = _photoUploadState.asStateFlow()

    private val _saveProfileState = MutableStateFlow<SaveProfileState>(SaveProfileState.Initial)
    val saveProfileState: StateFlow<SaveProfileState> = _saveProfileState.asStateFlow()





    /*init {
        getDoctors()
    }*/

    fun getDoctors() {
        loading.value = true
        viewModelScope.launch {
            try {
                doctors.value = repository.getDoctors()
            } catch (e: Exception) {
                error.value = true
                errorMessage.value = "Impossible de récupérer les docteurs"
                println("Erreur récup Doctors: ${e.message}") // pour le debug log

            } finally {
                loading.value = false
            }
        }
    }





    fun updateDoctorProfile(userInfo: UserInfo) {
        viewModelScope.launch {
            _photoUploadState.value = PhotoUploadState.Loading
            println("Begin updating....")
            val doctorInfo = DoctorInformation(
                userId = userInfo.id.toLong(),
                specialty = userInfo.specialty,
                contact = userInfo.phoneNumber,
                experience = userInfo.experience.toIntOrNull() ?: 0,
                clinicAddress = userInfo.clinic.address ?: "",
                clinicName = userInfo.clinic.name ?: "",
                clinicMap = userInfo.clinic.mapLocation ?: "",
                breakstart= userInfo.breakTime.start,
                breakend= userInfo.breakTime.end,
                startworkTime = userInfo.startWorkTime,
                endworkTime = userInfo.endWorkTime,
                facebook = if (userInfo.hasFacebook) userInfo.facebookUrl else "",
                instagram = if (userInfo.hasInstagram) userInfo.instagramUrl else "",
                linkedin = if (userInfo.hasLinkedIn) userInfo.linkedInUrl else "",
                workOnWeekend = userInfo.worksOnWeekend,
                workEveryDay = userInfo.workEveryday,
                workingDay = userInfo.workingDays,
                photoUrl = null // Will come from backend after update
            )

            val photoBytes = userInfo.photoUri?.let { uri ->
                try {
                    context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                } catch (e: Exception) {
                    _photoUploadState.value = PhotoUploadState.Error("Error reading image: ${e.localizedMessage}")
                    return@launch
                }
            }
            println("Call Repository....")
            repository.updateDoctorProfile(
                //userId = readId(context)!!.toInt(),
                userId= 26,
                specialty = doctorInfo.specialty,
                experience = doctorInfo.experience,
                contact = doctorInfo.contact,
                clinicName = doctorInfo.clinicName,
                clinicAddress = doctorInfo.clinicAddress,
                clinicMap = doctorInfo.clinicMap,
                breakstart= doctorInfo.breakstart,
                breakend= doctorInfo.breakend,
                startworkTime = doctorInfo.startworkTime,
                endworkTime = doctorInfo.endworkTime,
                facebook = doctorInfo.facebook,
                instagram = doctorInfo.instagram,
                linkedin = doctorInfo.linkedin,
                workOnWeekend = doctorInfo.workOnWeekend,
                workEveryDay = doctorInfo.workEveryDay,
                workingDays = doctorInfo.workingDay,
                photoBytes = photoBytes
            ).collect { result ->
                when (result) {
                    is ApiResult.Loading -> _photoUploadState.value = PhotoUploadState.Loading
                    is ApiResult.Success -> {
                        println(result.data.message)
                    }
                    is ApiResult.Error -> {
                        _photoUploadState.value = PhotoUploadState.Error(result.message)
                    }
                }
            }
        }
    }


    // State classes
    sealed class ProfileState {
        object Initial : ProfileState()
    }

    sealed class WorkingDaysState {
        object Initial : WorkingDaysState()
    }

    sealed class PhotoUploadState {
        object Initial : PhotoUploadState()
        object Loading : PhotoUploadState()
        data class Error(val message: String) : PhotoUploadState()
    }

    sealed class SaveProfileState {
        object Initial : SaveProfileState()
    }

    // Form data class
    data class DoctorInformation(
        val userId: Long,
        val specialty: String,
        val contact: String,
        val experience: Int,
        val clinicName: String,
        val clinicAddress: String,
        val clinicMap: String,
        val breakstart: String,
        val breakend: String,
        val startworkTime: String,
        val endworkTime: String,
        val facebook: String,
        val instagram: String,
        val linkedin: String,
        val workOnWeekend: Boolean,
        val workEveryDay: Boolean,
        val workingDay: List<WorkingDay>,
        val photoUrl: String? = null
    )
}
