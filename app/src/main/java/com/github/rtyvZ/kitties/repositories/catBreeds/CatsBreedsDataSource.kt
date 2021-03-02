package com.github.rtyvZ.kitties.repositories.catBreeds

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.rtyvZ.kitties.common.Api
import com.github.rtyvZ.kitties.common.models.CatBreed
import com.github.rtyvZ.kitties.network.NetworkResponse

class CatsBreedsDataSource(private val api: Api, private val pageSize: Int) :
    PagingSource<Int, CatBreed>() {
    private var nextKey: Int? = 0

    override fun getRefreshKey(state: PagingState<Int, CatBreed>): Int? {
        return state.anchorPosition?.let { position ->
            val page = state.closestPageToPosition(position)
            page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
        }
    }

    private fun changeKeys() {
        nextKey = nextKey?.inc()
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CatBreed> {
        return when (val response = api.getAllCatsBreeds(nextKey, pageSize)) {
            is NetworkResponse.Success -> {
                if (response.body.isNotEmpty()) {
                    changeKeys()
                } else {
                    nextKey = null
                }
                LoadResult.Page(
                    data = response.body.map { it },
                    prevKey = null,
                    nextKey = nextKey
                )
            }
            is NetworkResponse.ApiError -> {
                LoadResult.Error(Throwable())
            }
            is NetworkResponse.UnknownError -> {
                LoadResult.Error(response.error!!)
            }
            is NetworkResponse.NetworkError -> {
                LoadResult.Error(response.error)
            }
            else -> {
                LoadResult.Error(Throwable())
            }
        }
    }
}