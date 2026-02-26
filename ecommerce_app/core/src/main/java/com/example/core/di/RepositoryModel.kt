package com.example.core.di

import com.example.core.network.repository.AddressRepository
import com.example.core.network.repository.AuthRepository
import com.example.core.network.repository.BannerRepository
import com.example.core.network.repository.CategoryRepository
import com.example.core.network.repository.CurrencyRepository
import com.example.core.network.repository.DeliveryRepository
import com.example.core.network.repository.GeneralSettingRepository
import com.example.core.network.repository.MapRepository
import com.example.core.network.repository.OrderItemRepository
import com.example.core.network.repository.OrderRepository
import com.example.core.network.repository.PaymentRepository
import com.example.core.network.repository.PaymentTypeRepository
import com.example.core.network.repository.ProductRepository
import com.example.core.network.repository.StoreRepository
import com.example.core.network.repository.SubCategoryRepository
import com.example.core.network.repository.UserRepository
import com.example.core.network.repository.VariantRepository
import org.koin.dsl.module

val repositoryModel = module {
    single { AddressRepository(get(), get()) }
    single { AuthRepository(get()) }
    single { BannerRepository(get(), get()) }
    single { CategoryRepository(get(), get()) }
    single { GeneralSettingRepository(get(), get()) }
    single { OrderItemRepository(get(), get()) }
    single { OrderRepository(get(), get()) }
    single { ProductRepository(get(), get()) }
    single { StoreRepository(get(), get()) }
    single { SubCategoryRepository(get(), get()) }
    single { UserRepository(get(), get()) }
    single { VariantRepository(get(), get()) }
    single { MapRepository(get()) }
    single { DeliveryRepository(get(), get()) }
    single { CurrencyRepository(get(), get()) }
    single { PaymentRepository(get(), get()) }
    single { PaymentTypeRepository(get(), get()) }
}