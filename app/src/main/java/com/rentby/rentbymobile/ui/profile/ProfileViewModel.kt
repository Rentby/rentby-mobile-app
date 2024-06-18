package com.rentby.rentbymobile.ui.profile

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
            val currentUser = session.value ?: return@launch
            val updatedUser = UserModel(currentUser.email, name, address, phoneNumber)
            repository.saveSession(updatedUser)
            session.value = updatedUser
            _isLoading.value = false
            _message.value = "Profile updated successfully"
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