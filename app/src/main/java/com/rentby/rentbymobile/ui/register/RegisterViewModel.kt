package com.rentby.rentbymobile.ui.register

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.gson.Gson
import com.rentby.rentbymobile.data.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun register(context: Context, name: String, address: String, phoneNumber: String) {
        viewModelScope.launch {
            try {
                val email = Firebase.auth.currentUser?.email.toString()
                val response = userRepository.register(name, email, address, phoneNumber)
                Log.d("Register", response.toString())
            } catch (e: HttpException) {
                Log.e("Register", "HTTP error occurred", e)
                val errorMessage = e.response()?.errorBody()?.string() ?: "An error occurred"
                Toast.makeText(context, "Registration failed: $errorMessage", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Log.e("Register", "An error occurred", e)
                Toast.makeText(context, "An unexpected error occurred: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}