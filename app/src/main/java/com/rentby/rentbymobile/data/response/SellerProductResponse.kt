package com.rentby.rentbymobile.data.response

import com.google.gson.annotations.SerializedName
import com.rentby.rentbymobile.data.model.ProductItem

data class SellerProductResponse(

	@field:SerializedName("totalItems")
	val totalItems: Int? = null,

	@field:SerializedName("totalPages")
	val totalPages: Int? = null,

	@field:SerializedName("currentPage")
	val currentPage: Int? = null,

	@field:SerializedName("products")
	val products: List<ProductsItem?>? = null
)

data class ProductsItem(

	@field:SerializedName("booked")
	val booked: Int? = null,

	@field:SerializedName("rent_price")
	val rentPrice: Int? = null,

	@field:SerializedName("product_id")
	val productId: String? = null,

	@field:SerializedName("url_photo")
	val urlPhoto: String? = null,

	@field:SerializedName("rating")
	val rating: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("product_name")
	val productName: String? = null,

	@field:SerializedName("seller_id")
	val sellerId: String? = null
)

fun ProductsItem.toProductItem(): ProductItem {
	return ProductItem(
		id = productId ?: "",
		name = productName ?: "",
		price = rentPrice?.toString() ?: "0",
		rating = rating?.toFloatOrNull() ?: 0.0f,
		imageUrl = urlPhoto ?: ""
	)
}
