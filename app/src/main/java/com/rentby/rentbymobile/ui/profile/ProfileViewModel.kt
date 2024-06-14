package com.rentby.rentbymobile.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rentby.rentbymobile.data.pref.UserModel
import com.rentby.rentbymobile.data.repository.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getSession() = userRepository.getSession().asLiveData()

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

    fun updateUser(user: UserModel) {
        viewModelScope.launch {
            userRepository.saveSession(user)
        }
    }
}
