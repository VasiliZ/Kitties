package com.github.rtyvZ.kitties.repositories.catBreeds

import androidx.paging.PagingState
import com.github.rtyvZ.kitties.common.Api
import com.github.rtyvZ.kitties.common.models.CatBreed
import com.github.rtyvZ.kitties.common.repository.BaseDataSource
import com.github.rtyvZ.kitties.network.NetworkResponse

class CatsBreedsDataSource(private val api: Api, private val pageSize: Int) :
    BaseDataSource<Int, CatBreed>() {

    override fun getRefreshKey(state: PagingState<Int, CatBreed>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CatBreed> {
        return when (val response = api.getAllCatsBreeds(nextKey, pageSize)) {
            is NetworkResponse.Success -> {
                if (response.body.isNotEmpty()) {
                    changeKey()
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