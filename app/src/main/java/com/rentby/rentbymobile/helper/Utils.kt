package com.rentby.rentbymobile.helper

import java.text.NumberFormat
import java.util.Locale

fun formatStringtoRP(nominal: String): String {
    return "Rp" + NumberFormat.getNumberInstance(Locale("id", "ID")).format(nominal.toInt())
}

fun formatInttoRp(nominal: Int): String {
    return "Rp" + NumberFormat.getNumberInstance(Locale("id", "ID")).format(nominal)
}