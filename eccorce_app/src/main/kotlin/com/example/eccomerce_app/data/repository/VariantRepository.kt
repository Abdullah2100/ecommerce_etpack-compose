package com.example.eccomerce_app.data.repository

import android.util.Log
import com.example.eccomerce_app.util.Secrets
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import com.example.eccomerce_app.data.NetworkCallHandler
import com.example.eccomerce_app.dto.VariantDto
import com.example.eccomerce_app.util.GeneralValue
import io.ktor.client.call.body
import io.ktor.client.request.headers
import kotlinx.io.IOException
import java.net.UnknownHostException


class VariantRepository(val client: HttpClient)  {
    suspend fun getVariant(pageNumber: Int): NetworkCallHandler {
        return try {
            val result = client.get(
                Secrets.getUrl() + "/Variant/all/${pageNumber}"
            ) {
                headers {
                    append(
                        HttpHeaders.Authorization,
                        "Bearer ${GeneralValue.authData?.refreshToken}"
                    )
                }
            }
            when (result.status) {
                HttpStatusCode.Companion.OK -> {
                    NetworkCallHandler.Successful(result.body<List<VariantDto>>())
                }

                HttpStatusCode.Companion.NoContent -> {
                    NetworkCallHandler.Error("No Data Found")
                }

                else -> {
                    NetworkCallHandler.Error(result.body())
                }
            }

        } catch (e: UnknownHostException) {

            return NetworkCallHandler.Error(e.message)

        } catch (e: IOException) {

            return NetworkCallHandler.Error(e.message)

        } catch (e: Exception) {

            return NetworkCallHandler.Error(e.message)
        }
    }

}