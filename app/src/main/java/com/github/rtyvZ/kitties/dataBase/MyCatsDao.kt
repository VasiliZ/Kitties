package com.github.rtyvZ.kitties.dataBase

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.github.rtyvZ.kitties.common.Strings
import com.github.rtyvZ.kitties.common.models.Cat

@Dao
interface MyCatsDao {
    @Query("Select * from Cat")
    fun getAllCats(): LiveData<List<Cat>?>?

    @Delete()
    fun delete(cat: Cat)

    @Insert
    fun insertCat(cat: Cat)
}

@Database(entities = [Cat::class], version = 1)
abstract class CatDatabase : RoomDatabase() {
    abstract fun getCatDao(): MyCatsDao

    companion object {
        @Volatile
        private var INSTANCE: CatDatabase? = null

        fun getDatabase(context: Context): CatDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CatDatabase::class.java,
                    Strings.Const.DB_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}