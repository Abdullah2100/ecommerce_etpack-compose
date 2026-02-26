package com.example.core.network.dto

import kotlinx.serialization.Serializable

@Serializable()
data class GeneralSettingDto(

    val name: String,
    val value: Double
)