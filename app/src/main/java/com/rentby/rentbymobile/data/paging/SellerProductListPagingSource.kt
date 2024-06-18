package com.rentby.rentbymobile.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rentby.rentbymobile.data.response.ResultsItem
import com.rentby.rentbymobile.data.response.ProductsItem
import com.rentby.rentbymobile.data.retrofit.ApiService

class SellerProductListPagingSource(
    private val apiService: ApiService,
    private val sellerId: String
) : PagingSource<Int, ProductsItem>() {
    override fun getRefreshKey(state: PagingState<Int, ProductsItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductsItem> {
        val position = params.key ?: INITIAL_PAGE_INDEX
        return try {
            val response = apiService.getSellerProducts(sellerId, position)
            val results = response.products?.filterNotNull() ?: emptyList()
            val nextKey = if (results.isEmpty()) null else position + 1
            LoadResult.Page(
                data = results,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}