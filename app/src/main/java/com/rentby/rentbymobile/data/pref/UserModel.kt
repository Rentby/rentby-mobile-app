package com.rentby.rentbymobile.data.pref

data class UserModel(
    val email: String,
    val name: String,
    val address: String,
    val phoneNumber: String,
    val userId: String,
    val isRegistered: Boolean = false
)
