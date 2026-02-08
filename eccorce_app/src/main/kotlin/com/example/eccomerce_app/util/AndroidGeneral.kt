package com.example.eccomerce_app.util

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.e_commercompose.R
import com.example.eccomerce_app.data.Room.Model.Currency
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import java.io.File
import java.util.Locale

object AndroidGeneral {

    fun encryptionFactory(databaseName: String): SupportFactory {
        val passPhraseBytes = SQLiteDatabase.getBytes(databaseName.toCharArray())
        return SupportFactory(passPhraseBytes)
    }

    fun handlingImageForCoil(imageUrl: String?, context: Context): ImageRequest? {
        if (imageUrl == null) return null
        return when (imageUrl.endsWith(".svg")) {
            true -> {
                ImageRequest.Builder(context)
                    .data(imageUrl)
                    .decoderFactory(SvgDecoder.Factory())
                    .build()
            }

            else -> {
                ImageRequest.Builder(context)
                    .data(imageUrl)
                    .build()
            }

        }
    }


    fun convertColorToInt(value: String): Color? {
        return try {
            Color(value.toColorInt())
        } catch (ex: Exception) {
            null
        }
    }

    fun whenLanguageUpdateDo(locale: String, context: Context) {
//        val locale = Locale(locale)
//        Locale.setDefault(locale)
//
//        val config = context.resources.configuration
//        config.setLocale(locale)
//        config.setLayoutDirection(locale)
//
//        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    fun Uri.toCustomFil(context: Context): File? {
        var file: File? = null

        try {
            val resolver = context.contentResolver
            resolver.query(this, null, null, null, null)
                .use { cursor ->
                    if (cursor == null) throw Exception("could not accesses Local Storage")

                    cursor.moveToFirst()
                    val column = arrayOf(MediaStore.Images.Media.DATA)
                    val filePath = cursor.getColumnIndex(column[0])
                    file = File(cursor.getString(filePath))

                }
            return file
        } catch (e: Exception) {
            throw e
        }
    }
}
