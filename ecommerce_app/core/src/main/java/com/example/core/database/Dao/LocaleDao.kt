package com.example.core.database.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.core.database.Model.CurrentLocal

@Dao
interface LocaleDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun saveCurrentLocale(currentLocal: CurrentLocal)


    @Query("SELECT * FROM CurrentLocal where id=0")
    suspend fun getCurrentLocal(): CurrentLocal?
}