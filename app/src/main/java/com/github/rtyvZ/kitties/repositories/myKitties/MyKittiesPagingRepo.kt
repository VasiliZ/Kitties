package com.github.rtyvZ.kitties.repositories.myKitties

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.github.rtyvZ.kitties.common.models.Cat
import com.github.rtyvZ.kitties.db.MyCatsDao
import kotlinx.coroutines.flow.Flow

class MyKittiesPagingRepo(private val dao: MyCatsDao) {
    fun getKittiesFromDB(): Flow<PagingData<Cat>> {
        return Pager(
            PagingConfig(
                pageSize = 15,
                enablePlaceholders = false,
                prefetchDistance = 1
            )
        ) {
            dao.getAllCats()
        }.flow
    }
}