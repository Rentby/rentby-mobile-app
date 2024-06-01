package com.rentby.rentbymobile.data.request

data class RegisterRequest(
    val name: String,
    val email: String,
    val address: String,
    val phone_number: String
)