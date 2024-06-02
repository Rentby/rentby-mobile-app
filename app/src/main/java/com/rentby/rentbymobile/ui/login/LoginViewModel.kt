package com.rentby.rentbymobile.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rentby.rentbymobile.data.pref.UserModel
import com.rentby.rentbymobile.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun isRegistered(email: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val userDetail = userRepository.getUserDetail(email)
                if (userDetail != null) {
                    val name = userDetail.data?.name.toString()
                    val address = userDetail.data?.address.toString()
                    val phoneNumber = userDetail.data?.phoneNumber.toString()
                    userRepository.saveSession(UserModel(email, name, address, phoneNumber))
                    onResult(true)
                } else {
                    // User is not registered
                    onResult(false)
                }
            } catch (e: Exception) {
                // Handle any other exceptions
                onResult(false)
            }
        }
    }
}