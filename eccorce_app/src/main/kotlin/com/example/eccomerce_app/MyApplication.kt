package com.example.eccomerce_app

import android.app.Application
import com.example.eccomerce_app.di.*
import com.stripe.stripeterminal.TerminalApplicationDelegate
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(
                dataBaseModule,
                httpClientModule,
                viewModelModel,
                repositoryModel,
                webSocketClientModule,
                coroutineScopModel

            )
        }
        TerminalApplicationDelegate.onCreate(this)
    }
}