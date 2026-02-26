package com.example.core.database.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AuthModelEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = 0,
    val token: String = "",
    val refreshToken: String = ""
)