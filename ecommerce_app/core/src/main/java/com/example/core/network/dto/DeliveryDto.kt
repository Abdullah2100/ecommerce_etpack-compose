package com.example.core.network.dto

import com.example.common.model.UserModel
import com.example.core.network.kSerializeChanger.UUIDKserialize
import kotlinx.serialization.Serializable
import java.util.UUID
@Serializable
data class DeliveryDto(
    @Serializable(with = UUIDKserialize::class)
    val id: UUID,
    @Serializable(with = UUIDKserialize::class)
    val userId: UUID,
    val isAvailable: Boolean,
    val thumbnail: String? = null,
    val address: AddressDto,
    val user: DeliveryUserInfoDto,
    val analyse:DeliveryAnalyseDto?=null
)


@Serializable()
data class DeliveryAnalyseDto(
    val dayFee: Double? = null,
    val weekFee: Double? = null,
    val monthFee: Double? = null,
    val dayOrder: Int? = null,
    val weekOrder: Int? = null
)


@Serializable
data class DeliveryUserInfoDto(
    val name: String,
    val phone: String,
    val email: String,
    val thumbnail: String?=null,
) {
}

