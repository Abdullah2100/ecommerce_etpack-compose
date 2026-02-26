package com.example.core.network.dto

import com.example.core.network.kSerializeChanger.UUIDKserialize
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class PaymentTypeDto(
    @Serializable(with = UUIDKserialize::class)
    val id: UUID,
    val name: String,
    val isHashCheckOperation: Boolean,
    val thumbnail: String
)
