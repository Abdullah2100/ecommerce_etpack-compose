package com.example.common.model

import java.util.UUID

data class SubCategoryUpdate(
    val name: String,
    val id: UUID,
    val cateogyId: UUID,
)
