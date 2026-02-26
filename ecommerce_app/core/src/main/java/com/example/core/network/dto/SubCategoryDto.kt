package com.example.core.network.dto

import com.example.core.network.kSerializeChanger.UUIDKserialize
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class SubCategoryDto(
    val name: String,
    @Serializable(with = UUIDKserialize::class)
    val id: UUID,
    @Serializable(with = UUIDKserialize::class)
    val storeId: UUID,
    @Serializable(with = UUIDKserialize::class)
    val categoryId: UUID,
)


@Serializable
data class UpdateSubCategoryDto(
    val name: String,
    @Serializable(with = UUIDKserialize::class)
    val id: UUID,
    @Serializable(with = UUIDKserialize::class)
    val categoryId: UUID
)

@Serializable
data class CreateSubCategoryDto(
    val Name: String,
    @Serializable(with = UUIDKserialize::class)
    val CategoryId: UUID
)

