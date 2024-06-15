package com.rentby.rentbymobile.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rentby.rentbymobile.data.model.ProductItem
import com.rentby.rentbymobile.data.response.ResultsItem
import com.rentby.rentbymobile.data.retrofit.ApiService

class ProductCategoryPagingSource(
    private val apiService: ApiService,
    private val category: String
) : PagingSource<Int, ResultsItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResultsItem> {
        val currentPage = params.key ?: INITIAL_PAGE_INDEX
        val offset = if (params.key != null) ((currentPage - 1) * NETWORK_PAGE_SIZE) + 1 else INITIAL_PAGE_INDEX
        return try {
            val response = apiService.searchCategory(category, params.loadSize, offset)
            val products = response.results?.filterNotNull() ?: emptyList()

            val nextKey = if (products.isEmpty()) {
                null
            } else {
                currentPage + (params.loadSize / NETWORK_PAGE_SIZE)
            }

            // Log the successful API response
            Log.d("ProductCategoryPaging", "Loaded page $currentPage, items: ${products.size}")

            LoadResult.Page(
                data = products,
                prevKey = if (currentPage == INITIAL_PAGE_INDEX) null else currentPage - 1,
                nextKey = nextKey
            )
        } catch (exception: Exception) {
            // Log the error
            Log.e("ProductCategoryPaging", "Failed to load data", exception)
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ResultsItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 0
        const val NETWORK_PAGE_SIZE = 10
    }
}