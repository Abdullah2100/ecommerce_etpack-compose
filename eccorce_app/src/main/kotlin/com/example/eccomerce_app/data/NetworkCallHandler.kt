package com.example.eccomerce_app.data

sealed interface NetworkCallHandler {
    data class Successful<out T>(val data: T) : NetworkCallHandler
    data class Error(val data: String?) : NetworkCallHandler
}