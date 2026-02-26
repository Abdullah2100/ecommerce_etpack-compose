package com.example.core.network.repository

import com.example.core.network.NetworkCallHandler
import com.example.core.network.dto.GooglePlacesInfo
import com.example.core.database.Dao.AuthDao
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.UnknownHostException

class MapRepository(private val client: HttpClient ) {

    suspend fun getDistanceBetweenTwoPoint(
        origin: Pair<Double, Double>,
        destination: Pair<Double, Double>,
        key: String
    ): NetworkCallHandler = withContext(Dispatchers.IO) {
        val url = "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.first},${origin.second}&destination=${destination.first},${destination.second}&key=$key"

        return@withContext try {
            val result = client.get(url)
            when (result.status) {
                HttpStatusCode.Companion.OK -> {
                    NetworkCallHandler.Successful(result.body<GooglePlacesInfo>())
                }

                else -> {
                    NetworkCallHandler.Error(
                        result.body<String>()
                    )
                }
            }

        } catch (e: UnknownHostException) {

            return@withContext NetworkCallHandler.Error(e.message)

        } catch (e: IOException) {

            return@withContext NetworkCallHandler.Error(e.message)

        } catch (e: Exception) {

            return@withContext NetworkCallHandler.Error(e.message)
        }
    }

}