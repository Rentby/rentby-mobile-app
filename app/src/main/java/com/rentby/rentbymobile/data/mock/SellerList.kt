package com.rentby.rentbymobile.data.mock

import com.rentby.rentbymobile.R
import com.rentby.rentbymobile.data.model.Order
import com.rentby.rentbymobile.data.model.Seller

object SellerList {
    fun getSeller(): List<Seller> {
        return listOf(
            Seller(
                "seller1234",
                "Makassar Hiking",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/8/89/TheNorthFace_logo.svg/1200px-TheNorthFace_logo.svg.png",
                "https://api.whatsapp.com/send?phone=6282237100075",
                12,
                "Makassar, Sulawesi Selatan",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam risus felis, pretium at mauris non, ullamcorper sollicitudin eros. Aliquam ornare lacus eu lectus sodales dignissim. Etiam porta, enim sit amet dictum iaculis, nulla mauris vehicula diam, ac molestie nibh nisi nec mi. Curabitur non nisi ac diam auctor varius. Aenean sollicitudin vestibulum magna, vitae pellentesque massa viverra a. Maecenas sed pharetra purus. Mauris eget bibendum sem, sit amet sagittis nisi. Suspendisse et urna blandit, accumsan felis eget, imperdiet mauris. Nullam metus justo, interdum eu venenatis vel, fringilla non lorem.\n" +
                        "\n" +
                        "Sed rutrum eu dolor eu dictum. Nunc at tempus lectus, id lobortis leo. In ultricies nisl non diam tristique, congue tempor quam scelerisque. Fusce at porta felis, sit amet vulputate nunc. Curabitur vitae dapibus lectus. Curabitur fringilla ornare facilisis. Vestibulum non pretium orci, vitae dignissim neque. Nulla sed metus ut dolor tempus consequat et sit amet dui. Vivamus et lacinia dui. In porttitor sapien in lacus interdum porttitor. Sed ac rutrum lacus. Aliquam pretium euismod arcu sed aliquet. Suspendisse non sem quis mauris mollis pellentesque in at lorem. Donec rhoncus vulputate nisi, sit amet tempus quam finibus vitae.\n"
            )
        )
    }
}