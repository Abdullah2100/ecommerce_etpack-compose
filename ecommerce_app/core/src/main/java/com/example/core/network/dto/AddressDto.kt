package com.example.core.network.dto

import com.example.core.network.kSerializeChanger.UUIDKserialize
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class AddressDto(
    @Serializable(with = UUIDKserialize::class)
    val id: UUID,
    val longitude: Double,
    val latitude: Double,
    val title: String?,
    val isCurrent: Boolean?=false
)


@Serializable
data class CreateAddressDto(
    val Longitude: Double,
    val Latitude: Double,
    val Title: String?
)

@Serializable
data class AddressWithTitleDto(
    val longitude: Double,
    val latitude: Double,
    val title: String?=null
)

@Serializable
data class UpdateAddressDto(
    @Serializable(with = UUIDKserialize::class)
    val Id: UUID?,
    val Longitude: Double?,
    val Latitude: Double?,
    val Title: String?
)

