package com.github.rtyvZ.kitties.repositories.randomCatsRepository

import androidx.paging.PagingState
import com.github.rtyvZ.kitties.common.Api
import com.github.rtyvZ.kitties.common.UserInternalStorageContract
import com.github.rtyvZ.kitties.common.models.Cat
import com.github.rtyvZ.kitties.common.repository.BaseDataSource
import com.github.rtyvZ.kitties.network.NetworkResponse
import com.github.rtyvZ.kitties.network.response.MyVoteResponse

class KittySource(
    private val api: Api,
    private val sessionStorage: UserInternalStorageContract
) : BaseDataSource<Int, Cat>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Cat> {
        sessionStorage.let {
            return when (val response = api.getListKitties(nextKey, LIMIT_CATS)) {
                is NetworkResponse.Success -> {
                    val listKitties = mutableListOf<Cat>()
                    val listVotes = mutableListOf<MyVoteResponse>()
                    listKitties.addAll(response.body.map { it.toCat() })
                    changeKey()

                    when (val votes = api.getMyVotes(it.getSession()!!.userId)) {
                        is NetworkResponse.Success -> {
                            listVotes.addAll(votes.body)
                        }
                        is NetworkResponse.NetworkError -> {
                            votes.error
                        }
                        is NetworkResponse.UnknownError -> {
                            votes.let {
                                it.error
                            }
                        }
                        is NetworkResponse.ApiError -> {
                            Throwable("wtf")
                        }
                    }

                    listVotes?.let {
                        listKitties.forEachIndexed { index, catResponse ->
                            listVotes.forEach { votes ->
                                if (catResponse.id == votes.imageId) {
                                    listKitties[index] = Cat(
                                        id = catResponse.id,
                                        url = catResponse.url,
                                        width = catResponse.width,
                                        height = catResponse.height,
                                        choice = votes.voteValue,
                                        voteId = votes.idVote.toInt()
                                    )
                                }
                            }
                        }
                    }
                    LoadResult.Page(listKitties, null, nextKey = nextKey)
                }
                is NetworkResponse.NetworkError -> {
                    LoadResult.Error(response.error)
                }
                is NetworkResponse.UnknownError -> {
                    LoadResult.Error(response.error!!)
                }
                is NetworkResponse.ApiError -> {
                    LoadResult.Error(Throwable("wtf"))
                }
                else -> {
                    LoadResult.Error(Throwable())
                }
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Cat>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey
        }
    }

    companion object {
        const val LIMIT_CATS = 15
    }
}