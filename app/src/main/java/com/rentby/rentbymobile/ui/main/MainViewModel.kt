package com.rentby.rentbymobile.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.rentby.rentbymobile.data.pref.UserModel
import com.rentby.rentbymobile.data.repository.UserRepository

class MainViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }
}