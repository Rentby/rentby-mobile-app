package com.rentby.rentbymobile.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rentby.rentbymobile.data.model.ProductItem
import com.rentby.rentbymobile.data.retrofit.ApiService

class ProductCategoryPagingSource(
    private val apiService: ApiService,
    private val category: String
) : PagingSource<Int, ProductItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductItem> {
        return try {
            val currentPage = params.key ?: 0
            val response = apiService.searchCategory(category, params.loadSize, currentPage * params.loadSize)
            val data = response.results?.filterNotNull()?.map { resultsItem ->
                ProductItem(
                    id = resultsItem.productId ?: "",
                    name = resultsItem.productName ?: "",
                    price = resultsItem.rentPrice?.toString() ?: "0",
                    rating = resultsItem.rating?.toFloatOrNull() ?: 0.0f,
                    imageUrl = resultsItem.urlPhoto ?: ""
                )
            } ?: emptyList()

            // Log the successful API response
            Log.d("ProductCategoryPaging", "Loaded page $currentPage, items: ${data.size}")

            LoadResult.Page(
                data = data,
                prevKey = if (currentPage == 0) null else currentPage - 1,
                nextKey = if (data.isEmpty()) null else currentPage + 1
            )
        } catch (exception: Exception) {
            // Log the error
            Log.e("ProductCategoryPaging", "Failed to load data", exception)
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ProductItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}