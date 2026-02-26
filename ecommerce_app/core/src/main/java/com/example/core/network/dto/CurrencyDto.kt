package com.example.core.network.dto

import com.example.core.network.kSerializeChanger.UUIDKserialize
import kotlinx.serialization.Serializable
import java.util.UUID


@Serializable
data class CurrencyDto(
    @Serializable(with = UUIDKserialize::class)
    var id: UUID,
    var name: String,
    var symbol: String,
    var value: Int,
    var isDefault: Boolean
)
