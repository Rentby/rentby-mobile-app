package com.rentby.rentbymobile.ui.profile

import android.util.Log
import androidx.lifecycle.*
import com.rentby.rentbymobile.data.pref.UserModel
import com.rentby.rentbymobile.data.repository.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {

    private val session = MutableLiveData<UserModel>()
    private val _isLoading = MutableLiveData<Boolean>()
    private val _message = MutableLiveData<String>()

    val isLoading: LiveData<Boolean>
        get() = _isLoading

    val message: LiveData<String>
        get() = _message

    init {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                session.value = user
            }
        }
    }

    fun getSession(): LiveData<UserModel> {
        return session
    }

    fun updateProfile(name: String, address: String, phoneNumber: String) {
        _isLoading.value = true

        viewModelScope.launch {
            session.value?.let { user ->
                repository.updateUser(
                    user.email,
                    name,
                    phoneNumber,
                    address,
                    onResult = {
                        _message.value = "Profile updated successfully"

                        updateSession(user.email) { success ->
                            if (!success) {
                                _message.value = "Failed to update session"
                            }
                        }
                        _isLoading.value = false

                    },
                    onError = {
                        _isLoading.value = false
                        _message.value = "Failed to update profile"
                    }
                )
            }
        }
    }

    fun updateSession(email: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val userDetail = repository.getUserDetail(email)
                if (userDetail != null) {
                    val name = userDetail.name.toString()
                    val address = userDetail.address.toString()
                    val phoneNumber = userDetail.phoneNumber.toString()
                    val userId = userDetail.userId.toString()
                    repository.saveSession(UserModel(email, name, address, phoneNumber, userId))

                    viewModelScope.launch {
                        repository.getSession().collect { user ->
                            session.value = user
                        }
                    }

                    onResult(true)
                } else {
                    _isLoading.value = false
                    onResult(false)
                }
            } catch (e: Exception) {
                _isLoading.value = false
                onResult(false)
            }
        }
    }

    fun clearMessage() {
        _message.value = ""
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _message.value = "Logged out successfully"
        }
    }
}