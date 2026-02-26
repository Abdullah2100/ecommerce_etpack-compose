package com.example.common.model

import java.util.UUID

data class OrderProduct(
    val id: UUID,
    val name: String,
    val thmbnail: String?,
    val storeId: UUID
)