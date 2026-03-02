package com.example.e_commerce_delivery_man.dto.response

import com.example.hotel_mobile.services.kSerializeChanger.UUIDKserialize
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data  class VariantDto(
    @Serializable(with= UUIDKserialize::class)
    val id:UUID,
    val name:String
)