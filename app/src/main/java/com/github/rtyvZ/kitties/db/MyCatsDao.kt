package com.github.rtyvZ.kitties.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.github.rtyvZ.kitties.common.models.Cat

@Dao
interface MyCatsDao {
    @Query("Select * from Cat")
    fun getAllCats(): List<Cat>

    @Delete()
    fun delete(cat: Cat)

    @Insert
    fun insertCat(cat: Cat)
}

@Database(entities = [Cat::class], version = 1)
abstract class CatDatabase : RoomDatabase() {
    abstract fun getCatDao(): MyCatsDao
}