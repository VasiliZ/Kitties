package com.github.rtyvZ.kitties.domain.randomCat

import com.github.rtyvZ.kitties.common.models.Cat
import com.github.rtyvZ.kitties.network.NetworkResponse
import com.github.rtyvZ.kitties.network.data.CatResponse
import com.github.rtyvZ.kitties.network.response.CatResponseVoteAndFav
import com.github.rtyvZ.kitties.repositories.RandomCatsRepository.RandomCatsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RandomCatsModel @Inject constructor(
    private val repository: RandomCatsRepository
) : RandomCatsModelContract {

    override fun getKittiesFromNet(): Flow<NetworkResponse<List<CatResponse>, Any>> =
        repository.getKitties()

    override fun voteForCat(cat: Cat): Flow<NetworkResponse<CatResponseVoteAndFav, Any>> =
        repository.voteForCat(cat)

    override fun deleteVoteForCat(cat: Cat): Flow<NetworkResponse<CatResponseVoteAndFav, Any>> =
        repository.deleteVote(cat)

    override fun addCatToFavorite(id: String): Flow<NetworkResponse<CatResponseVoteAndFav, Any>> =
        repository.addToFavorite(id)

    fun getVotes() = repository.getVotes()
}