package com.rentby.rentbymobile.data.request

class MakeOrderRequest (
    val product_id: String,
    val user_id: String,
    val rent_start: String,
    val rent_end: String
)