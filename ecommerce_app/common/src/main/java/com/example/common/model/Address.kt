package com.example.common.model

import kotlinx.serialization.Serializable
import java.util.UUID


data class Address(
    val id: UUID?,
    val longitude: Double,
    val latitude: Double,
    val title: String?,
    val isCurrent: Boolean
)



data class AddressWithTitle(
    val longitude: Double,
    val latitude: Double,
    val title: String?=null
)

enum class enMapType{My,MyStore,Store,TrackOrder}