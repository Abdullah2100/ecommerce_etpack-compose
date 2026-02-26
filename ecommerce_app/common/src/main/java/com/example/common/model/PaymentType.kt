package com.example.common.model

import java.util.UUID

data class PaymentType(
    val id: UUID,
    val name: String,
    val isHashCheckOperation: Boolean,
    val thumbnail: String
)
