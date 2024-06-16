package com.rentby.rentbymobile.data.response

import com.google.gson.annotations.SerializedName

data class ProductListResponse(

	@field:SerializedName("nextPage")
	val nextPage: Int? = null,

	@field:SerializedName("hasMore")
	val hasMore: Boolean? = null,

	@field:SerializedName("results")
	val results: List<ResultsItem?>? = null
)

data class ResultsItem(

	@field:SerializedName("url_photo")
	val urlPhoto: String? = null,

	@field:SerializedName("product_id")
	val productId: String? = null,

	@field:SerializedName("rent_price")
	val rentPrice: Int? = null,

	@field:SerializedName("rating")
	val rating: String? = null,

	@field:SerializedName("product_name")
	val productName: String? = null
)
