package com.example.core.network.dto

import com.example.core.network.kSerializeChanger.UUIDKserialize
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class OrderProductDto(
    @Serializable(with = UUIDKserialize::class)
    val id: UUID,
    @Serializable(with = UUIDKserialize::class)
    val storeId: UUID,
    val name: String,
    val thmbnail: String?=null
)