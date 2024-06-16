package com.rentby.rentbymobile.data.model

import androidx.annotation.DrawableRes

data class ProductMock(
    val id: String,
    val name: String,
    val price: String,
    val rating: Float,
    val booked: Int,
    val location: String,
    val sellerId: String,
    @DrawableRes
    val image: Int?,
    val description: String
)
