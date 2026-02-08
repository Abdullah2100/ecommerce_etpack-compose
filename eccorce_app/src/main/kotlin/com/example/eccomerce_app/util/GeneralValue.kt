package com.example.eccomerce_app.util

import com.example.eccomerce_app.data.Room.Model.AuthModelEntity
import com.example.eccomerce_app.dto.AuthDto
import kotlinx.coroutines.flow.MutableStateFlow

class GeneralValue {
    companion object{
        var authData :AuthModelEntity?=null
    }
}