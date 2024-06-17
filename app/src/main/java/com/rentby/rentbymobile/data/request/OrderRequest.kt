package com.rentby.rentbymobile.data.request

data class OrderRequest (
    val product_id: String,
    val user_id: String,
    val rent_start: String,
    val rent_end: String,
    val rent_length: Int
)