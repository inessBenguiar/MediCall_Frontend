package com.example.medicall.viewmodel



import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medicall.entity.ClinicResponse
import com.example.medicall.entity.Doctor
import com.example.medicall.repository.DoctorRepository
import com.example.medicall.service.ApiResult
import com.example.medicall.ui.components.WorkingDay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DoctorModel(private val repository: DoctorRepository): ViewModel() {
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

    // Local state for form data
    private val _doctorInfo = MutableStateFlow<DoctorInformation?>(null)
    val doctorInfo: StateFlow<DoctorInformation?> = _doctorInfo.asStateFlow()

    private val _workingDays = MutableStateFlow<List<WorkingDay>>(emptyList())
    val workingDays: StateFlow<List<WorkingDay>> = _workingDays.asStateFlow()

    private val _clinics = mutableStateListOf<ClinicResponse>()
    val clinics: List<ClinicResponse> = _clinics



    init {
        getDoctors()
    }

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
    fun navigateToDetail(
        firstName: String,
        familyName: String,
        photoUrl: String,
        address: String,
        phone: String,
        email: String
    ) {

    }

    // Functions to fetch data
    /*fun loadDoctorProfile(doctorId: Long) {
        viewModelScope.launch {
            _profileState.value = ProfileState.Loading

            repository.getDoctorProfile(doctorId).collect { result ->
                when (result) {
                    is ApiResult.Loading -> _profileState.value = ProfileState.Loading
                    is ApiResult.Success -> {
                        val doctor = result.data
                        _profileState.value = ProfileState.Success(doctor)

                        // Update the local form state
                        _doctorInfo.value = DoctorInformation(
                            userId = doctor.user_id,
                            specialty = doctor.specialty,
                            contact = doctor.contact ?: "",
                            experience = doctor.experience ?: 0,
                            clinic = doctor.address ?: "",
                            facebook = doctor.facebook ?: "",
                            instagram = doctor.instagram ?: "",
                            linkedin = doctor.linkedin ?: "",
                            workOnWeekend = doctor.workOnWeekend ?: false,
                            photoUrl = doctor.photo
                        )
                    }
                    is ApiResult.Error -> _profileState.value = ProfileState.Error(result.message)
                }
            }
        }
    }*/

    fun loadWorkingDays(doctorId: Long) {
        viewModelScope.launch {
            _workingDaysState.value = WorkingDaysState.Loading

            repository.getWorkingDays(doctorId).collect { result ->
                when (result) {
                    is ApiResult.Loading -> _workingDaysState.value = WorkingDaysState.Loading
                    is ApiResult.Success<*> -> {
                        _workingDays.value = result.data as List<WorkingDay>
                        _workingDaysState.value = WorkingDaysState.Success(result.data)
                    }
                    is ApiResult.Error -> _workingDaysState.value = WorkingDaysState.Error(result.message)
                }
            }
        }
    }

    // Functions to update data
    fun updateDoctorInfo(doctorInfo: DoctorInformation) {
        _doctorInfo.update { doctorInfo }
    }

    fun updateWorkingDay(index: Int, workingDay: WorkingDay) {
        val currentList = _workingDays.value.toMutableList()
        if (index in currentList.indices) {
            currentList[index] = workingDay
            _workingDays.value = currentList
        }
    }

    /*fun saveProfileInformation() {
        viewModelScope.launch {
            _doctorInfo.value?.let { info ->
                _saveProfileState.value = SaveProfileState.Loading

                repository.updateDoctorProfile(
                    doctorId = info.userId,
                    specialty = info.specialty,
                    experience = info.experience,
                    contact = info.contact,
                    clinic = info.clinic,
                    facebook = info.facebook.takeIf { it.isNotBlank() },
                    instagram = info.instagram.takeIf { it.isNotBlank() },
                    linkedin = info.linkedin.takeIf { it.isNotBlank() },
                    workOnWeekend = info.workOnWeekend
                ).collect { result ->
                    when (result) {
                        is ApiResult.Loading -> _saveProfileState.value = SaveProfileState.Loading
                        is ApiResult.Success -> _saveProfileState.value = SaveProfileState.Success
                        is ApiResult.Error -> _saveProfileState.value = SaveProfileState.Error(result.message)
                    }
                }
            } ?: run {
                _saveProfileState.value = SaveProfileState.Error("No doctor information available")
            }
        }
    }*/

    fun saveWorkingDays(doctorId: Long) {
        viewModelScope.launch {
            _workingDaysState.value = WorkingDaysState.Saving

            repository.updateWorkingDays(
                doctorId,
                _workingDays.value
            ).collect { result ->
                when (result) {
                    is ApiResult.Loading -> _workingDaysState.value = WorkingDaysState.Saving
                    is ApiResult.Success -> _workingDaysState.value = WorkingDaysState.SaveSuccess
                    is ApiResult.Error -> _workingDaysState.value = WorkingDaysState.Error(result.message)
                }
            }
        }
    }



    // Reset states
    fun resetSaveState() {
        _saveProfileState.value = SaveProfileState.Initial
    }

    fun resetPhotoUploadState() {
        _photoUploadState.value = PhotoUploadState.Initial
    }

    // State classes
    sealed class ProfileState {
        object Initial : ProfileState()
        object Loading : ProfileState()
        data class Success(val doctor: Doctor) : ProfileState()
        data class Error(val message: String) : ProfileState()
    }

    sealed class WorkingDaysState {
        object Initial : WorkingDaysState()
        object Loading : WorkingDaysState()
        object Saving : WorkingDaysState()
        object SaveSuccess : WorkingDaysState()
        data class Success(val workingDays: List<WorkingDay>) : WorkingDaysState()
        data class Error(val message: String) : WorkingDaysState()
    }

    sealed class PhotoUploadState {
        object Initial : PhotoUploadState()
        object Loading : PhotoUploadState()
        data class Success(val photoUrl: String) : PhotoUploadState()
        data class Error(val message: String) : PhotoUploadState()
    }

    sealed class SaveProfileState {
        object Initial : SaveProfileState()
        object Loading : SaveProfileState()
        object Success : SaveProfileState()
        data class Error(val message: String) : SaveProfileState()
    }

    // Form data class
    data class DoctorInformation(
        val userId: Long,
        val specialty: String,
        val contact: String,
        val experience: Int,
        val clinic: String,
        val facebook: String,
        val instagram: String,
        val linkedin: String,
        val workOnWeekend: Boolean,
        val photoUrl: String? = null
    )
}
