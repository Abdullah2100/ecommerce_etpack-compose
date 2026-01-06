package com.example.eccomerce_app.util

object Secrets {
    init {
        System.loadLibrary("nativelib")
    }

//    external fun getUrlFromNdk(): String

//    fun getUrl(): String = getUrlFromNdk()
//    fun getUrl(): String="http://72.60.232.89:5077/api"
    // fun getUrl(): String="http://192.168.1.109:5077/api"
    fun getUrl(): String="http://192.168.1.45:5077/api"

    //const val imageUrl = "192.168.1.109"
    const val imageUrl = "192.168.1.45"

}
