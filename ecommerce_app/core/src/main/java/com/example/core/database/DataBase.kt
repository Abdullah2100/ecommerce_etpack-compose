package com.example.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.core.database.Dao.AuthDao
import com.example.core.database.Dao.CurrencyDao
import com.example.core.database.Dao.LocaleDao
import com.example.core.database.Model.IsPassLocationScreen
import com.example.core.database.Model.AuthModelEntity
import com.example.core.database.Model.Currency
import com.example.core.database.Model.CurrentLocal
import com.example.core.database.Model.IsPassOnBoardingScreen

@Database(
    entities = [
        AuthModelEntity::class,
        IsPassOnBoardingScreen::class,
        IsPassLocationScreen::class,
        CurrentLocal::class,
        Currency::class
    ], version = 1, exportSchema = false
)
abstract class DataBase
    : RoomDatabase()
{
    abstract fun authDao(): AuthDao
    abstract fun currentLocal(): LocaleDao
    abstract fun currencyDao(): CurrencyDao
}