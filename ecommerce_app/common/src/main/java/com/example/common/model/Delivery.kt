package com.example.common.model

import kotlinx.serialization.Serializable
import java.util.UUID


data class Delivery(
    val id: UUID,
    val userId: UUID,
    val isAvailable: Boolean,
    val thumbnail: String? = null,
    val address: Address?=null,
    val user: DeliveryUserInfo,
    val analyse: DeliveryAnalyse?=null
)

data class DeliveryAnalyse(
    val dayFee: Double? = null,
    val weekFee: Double? = null,
    val monthFee: Double? = null,
    val dayOrder: Int? = null,
    val weekOrder: Int? = null
)


data class DeliveryUserInfo(
    val name: String,
    val phone: String,
    val email: String,
    val thumbnail: String?=null,
)
