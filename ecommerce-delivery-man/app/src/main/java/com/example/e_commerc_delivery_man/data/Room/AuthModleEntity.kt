package com.example.e_commerce_delivery_man.data.Room

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity("DeliveryAuthT")
data class AuthModleEntity
    (
    @PrimaryKey(autoGenerate = true) val id: Int? = 0,
    val token: String = "",
    val refreshToken: String = ""
)