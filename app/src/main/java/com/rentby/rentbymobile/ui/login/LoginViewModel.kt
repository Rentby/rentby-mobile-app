package com.rentby.rentbymobile.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rentby.rentbymobile.data.pref.UserModel
import com.rentby.rentbymobile.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading
    fun isRegistered(email: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val userDetail = userRepository.getUserDetail(email)
                if (userDetail != null) {
                    val name = userDetail.data?.name.toString()
                    val address = userDetail.data?.address.toString()
                    val phoneNumber = userDetail.data?.phoneNumber.toString()
                    userRepository.saveSession(UserModel(email, name, address, phoneNumber))
                    onResult(true)
                } else {
                    _loading.value = false
                    onResult(false)
                }
            } catch (e: Exception) {
                _loading.value = false
                onResult(false)
            }
        }
    }
}