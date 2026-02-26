package com.example.core.database.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.core.database.Model.AuthModelEntity
import com.example.core.database.Model.IsPassLocationScreen
import com.example.core.database.Model.IsPassOnBoardingScreen

@Dao
interface AuthDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAuthData(authData: AuthModelEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePassingOnBoarding(value: IsPassOnBoardingScreen)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePassingLocation(authData: IsPassLocationScreen)


    @Query("SELECT * FROM AuthModelEntity")
    suspend fun getAuthData(): AuthModelEntity?

    @Query("SELECT COUNT(*)>0 FROM IsPassOnBoardingScreen")
    fun isPassOnBoarding(): Boolean?

    @Query("SELECT count(*)>0 FROM location ")
    suspend fun isPassLocationScreen(): Boolean


    @Query("DELETE FROM AuthModelEntity ")
    suspend fun nukeAuthTable()

    @Query("DELETE FROM location ")
    suspend fun nukeIsPassAddressTable()

}
