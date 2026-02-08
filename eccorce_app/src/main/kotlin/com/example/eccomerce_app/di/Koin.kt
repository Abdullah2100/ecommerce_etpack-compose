package com.example.eccomerce_app.di

import org.koin.core.module.Module
import org.koin.dsl.module

fun platformModule(): Module = module {
    includes(dataBaseModule, webSocketClientModule)
}
