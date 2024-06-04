package com.rentby.rentbymobile.ui.register

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.gson.Gson
import com.rentby.rentbymobile.data.pref.UserModel
import com.rentby.rentbymobile.data.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading
    fun register(
        context: Context,
        name: String,
        address: String,
        phoneNumber: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val email = Firebase.auth.currentUser?.email.toString()
                val response = userRepository.register(name, email, address, phoneNumber)
                Log.d("Register", response.toString())

                userRepository.saveSession(UserModel(email, name, address, phoneNumber, true))
                onSuccess() // Notify the success
            } catch (e: HttpException) {
                _loading.value = false
                Log.e("Register", "HTTP error occurred", e)
                val errorMessage = e.response()?.errorBody()?.string() ?: "An error occurred"
                Toast.makeText(context, "Registration failed: $errorMessage", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                _loading.value = false
                Log.e("Register", "An error occurred", e)
                Toast.makeText(context, "An unexpected error occurred: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}