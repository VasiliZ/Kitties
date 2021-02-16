package com.github.rtyvZ.kitties.repositories.catBreeds

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.rtyvZ.kitties.common.Api
import com.github.rtyvZ.kitties.common.models.CatBreed
import com.github.rtyvZ.kitties.network.NetworkResponse

class CatsBreedsDataSource(private val api: Api, val pageSize:Int) : PagingSource<Int, CatBreed>() {
    private var prevKey = 0
    private var nextKey = 0

    override fun getRefreshKey(state: PagingState<Int, CatBreed>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey
        }
    }

    private fun changeKeys() {
        prevKey = nextKey
        nextKey++
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CatBreed> {
        return when (val response = api.getAllCatsBreeds(nextKey, pageSize)) {
            is NetworkResponse.Success -> {
                changeKeys()
                LoadResult.Page(
                    data = response.body.map { it },
                    prevKey = prevKey,
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
        }
    }
}