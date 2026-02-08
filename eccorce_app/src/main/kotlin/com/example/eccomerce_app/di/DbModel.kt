package com.example.eccomerce_app.di

import android.content.Context
import androidx.room.Room
import com.example.eccomerce_app.data.Room.DataBase
import com.example.eccomerce_app.util.Secrets
import com.example.eccomerce_app.util.General
import com.example.eccomerce_app.util.AndroidGeneral
import org.koin.dsl.module

fun provideDataBase(application: Context): DataBase {
    return Room
        .databaseBuilder(
            application,
            DataBase::class.java,
            "table_post")
        .openHelperFactory(AndroidGeneral.encryptionFactory(Secrets.getUrl()))
        .build()
}


val dataBaseModule = module {
    single { provideDataBase(application = get()) }
    single { get<DataBase>().authDao() }
    single { get<DataBase>().currentLocal() }
    single { get<DataBase>().currencyDao() }
}