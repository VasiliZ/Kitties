package com.github.rtyvZ.kitties.domain.randomCat

import com.github.rtyvZ.kitties.common.models.Cat
import com.github.rtyvZ.kitties.network.NetworkResponse
import com.github.rtyvZ.kitties.network.data.CatResponse
import com.github.rtyvZ.kitties.network.response.CatResponseVoteAndFav
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface RandomCatsModelContract {

  //  fun getKittiesFromNet(): Flow<NetworkResponse<List<CatResponse>, Any>>

    fun voteForCat(cat: Cat): Flow<NetworkResponse<CatResponseVoteAndFav, Any>>

    fun deleteVoteForCat(cat: Cat): Flow<NetworkResponse<CatResponseVoteAndFav, Any>>

    fun addCatToFavorite(id: String): Flow<NetworkResponse<CatResponseVoteAndFav, Any>>
}