package com.rentby.rentbymobile.data.response

import com.google.gson.annotations.SerializedName
import com.rentby.rentbymobile.data.model.Product

data class ProductDetailResponse(

	@field:SerializedName("booked")
	val booked: Int? = null,

	@field:SerializedName("rent_price")
	val rentPrice: Int? = null,

	@field:SerializedName("url_photo")
	val urlPhoto: String? = null,

	@field:SerializedName("product_id")
	val productId: String? = null,

	@field:SerializedName("rating")
	val rating: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("product_name")
	val productName: String? = null,

	@field:SerializedName("seller_id")
	val sellerId: String? = null
)

fun ProductDetailResponse.toProduct(): Product {
	return Product(
		id = this.productId ?: "",
		name = this.productName ?: "",
		rentPrice = this.rentPrice ?: 0,
		imageUrl = this.urlPhoto ?: "",
		rating = try {
			this.rating?.toFloat() ?: 0f
		} catch (e: NumberFormatException) {
			0f
		},
		booked = this.booked ?: 0,
		description = this.description ?: "",
		sellerId = this.sellerId ?: ""
	)
}
