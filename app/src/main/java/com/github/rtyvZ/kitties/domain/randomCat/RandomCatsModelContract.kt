package com.github.rtyvZ.kitties.domain.randomCat

import com.github.rtyvZ.kitties.common.models.Cat
import com.github.rtyvZ.kitties.network.response.CatResponseVoteAndFav
import kotlinx.coroutines.flow.Flow

interface RandomCatsModelContract {

    fun getKittiesFromNet(): Flow<List<Cat>>?

    fun voteForCat(cat: Cat): Flow<CatResponseVoteAndFav>

    fun deleteVoteForCat(cat: Cat): Flow<CatResponseVoteAndFav>

    fun addCatToFavorite(id: String): Flow<CatResponseVoteAndFav>
}