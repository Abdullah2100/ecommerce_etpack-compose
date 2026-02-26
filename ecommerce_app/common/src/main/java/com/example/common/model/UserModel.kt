package com.example.common.model

import java.io.File
import java.util.UUID



data class UserModel(
    val id: UUID,
    val name: String,
    val phone: String,
    val email: String,
    val thumbnail: String,
    val address: List<Address>? = null,
    val storeId: UUID?=null
)


data class UpdateMyInfo(
    val name:String?=null,
    val thumbnail: File?=null,
    val newPassword:String?=null,
    val oldPassword:String?=null,
    val phone:String?=null
)
