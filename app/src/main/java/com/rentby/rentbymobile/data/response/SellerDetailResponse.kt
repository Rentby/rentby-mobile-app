package com.rentby.rentbymobile.data.response

import com.google.gson.annotations.SerializedName
import com.rentby.rentbymobile.data.model.Product
import com.rentby.rentbymobile.data.model.Seller

data class SellerDetailResponse(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("product_total")
	val productTotal: Int? = null,

	@field:SerializedName("whatsapp_link")
	val whatsappLink: String? = null
)

fun SellerDetailResponse.toSeller(): Seller {
	return Seller(
		id = this.id ?: "",
		name = this.name ?: "",
		image = "",
		whatsappLink = this.whatsappLink ?: "",
		productTotal = this.productTotal ?: 0,
		location = this.location ?: "",
		description = ""
	)
}
