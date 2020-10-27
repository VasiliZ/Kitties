package com.github.rtyvZ.kitties.domain.randomCat

import com.github.rtyvZ.kitties.common.models.Cat
import com.github.rtyvZ.kitties.network.response.CatResponseVoteAndFav
import com.github.rtyvZ.kitties.repositories.RandomCatsRepository.RandomCatsRepository
import kotlinx.coroutines.flow.Flow

class RandomCatsModel(private val repository: RandomCatsRepository) : RandomCatsModelContract {

    override fun getKittiesFromNet(): Flow<List<Cat>>? = repository.getKitties()

    override fun voteForCat(cat: Cat): Flow<CatResponseVoteAndFav> = repository.voteForCat(cat)

    override fun deleteVoteForCat(cat: Cat): Flow<CatResponseVoteAndFav> =
        repository.deleteVote(cat)

    override fun addCatToFavorite(id: String): Flow<CatResponseVoteAndFav> =
        repository.addToFavorite(id)
}