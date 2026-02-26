package com.example.common.model

import java.util.UUID

data class BannerModel(
    val id: UUID,
    val image: String,
    val storeId: UUID
)
