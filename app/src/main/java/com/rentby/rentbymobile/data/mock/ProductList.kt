package com.rentby.rentbymobile.data

import com.rentby.rentbymobile.R
import com.rentby.rentbymobile.data.model.Product

object ProductList {
    fun getProducts(): ArrayList<Product> {
        return arrayListOf(
            Product("1", "Tenda Dome Kapasitas 4 Orang Waterproof Portable", "50.000", 4.8f, 25, R.drawable.default_image),
            Product("2", "Kompor Portable Mini", "25.000", 4.5f, 30, R.drawable.default_image),
            Product("3", "Tas Ransel Gunung Carrier 50L Backsupport Water Resistant", "40.000", 4.7f, 18, R.drawable.default_image),
            Product("4", "Sleeping Bag Waterproof dan Compact", "15.000", 4.6f, 22, R.drawable.default_image),
            Product("5", "Matras Gulung Camping Portable", "10.000", 4.2f, 35, R.drawable.default_image),
            Product("6", "Lampu Tenda LED Rechargeable", "20.000", 4.3f, 40, R.drawable.default_image),
            Product("7", "Alat Masak Portable Set untuk Camping", "30.000", 4.5f, 12, R.drawable.default_image),
            Product("8", "Jas Hujan Ponco Ultralight", "12.000", 4.1f, 28, R.drawable.default_image),
            Product("9", "Trekking Pole Adjustable Aluminium", "18.000", 4.4f, 15, R.drawable.default_image),
            Product("10", "Filter Air Portable untuk Mendaki", "35.000", 4.8f, 10, R.drawable.default_image)
        )
    }
}
