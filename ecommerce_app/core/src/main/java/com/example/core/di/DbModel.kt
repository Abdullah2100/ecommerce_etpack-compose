package com.example.core.di

import android.content.Context
import androidx.room.Room
import com.example.core.Util
import com.example.core.database.DataBase
import com.example.core.network.Secrets
import org.koin.dsl.module

fun provideDataBase(application: Context): DataBase {
    return Room
        .databaseBuilder(
            application,
            DataBase::class.java,
            "table_post")
        .openHelperFactory(Util.encryptionFactory(Secrets.getUrl()))
        .build()
}


val dataBaseModule = module {
    single { provideDataBase(application = get()) }
    single { get<DataBase>().authDao() }
    single { get<DataBase>().currentLocal() }
    single { get<DataBase>().currencyDao() }
}