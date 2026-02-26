package com.example.core.network.repository

import com.example.core.database.Model.AuthModelEntity
import com.example.core.network.dto.OrderItemDto
import com.example.core.network.dto.UpdateOrderItemStatusDto
import com.example.core.network.NetworkCallHandler
import com.example.core.database.Dao.AuthDao
import com.example.core.network.Secrets
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.UnknownHostException
import java.util.UUID

class OrderItemRepository(private val client: HttpClient, private val authDao: AuthDao) {

    suspend fun getAuthData(): AuthModelEntity? {
        return authDao.getAuthData()
    }

    suspend fun getMyOrderItemForStoreId(pageNumber: Int): NetworkCallHandler =
        withContext(Dispatchers.IO) {
            val authData =
                getAuthData() ?: return@withContext NetworkCallHandler.Error("user is not authenticate ")

            return@withContext try {
                val fullUrl = Secrets.getUrl() + "/orderItems/${pageNumber}"
                val result = client.get(fullUrl) {
                    headers {
                        append(
                            HttpHeaders.Authorization,
                            "Bearer ${authData.refreshToken}"
                        )
                    }

                }

                when (result.status) {
                    HttpStatusCode.Companion.OK -> {
                        NetworkCallHandler.Successful(result.body<List<OrderItemDto>>())
                    }

                    HttpStatusCode.Companion.NoContent -> {
                        NetworkCallHandler.Error("No Data Found")
                    }

                    else -> {
                        NetworkCallHandler.Error(result.body<String>())
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

    suspend fun updateOrderItemStatus(id: UUID, status: Int): NetworkCallHandler =
        withContext(Dispatchers.IO) {
            val authData =
                getAuthData() ?: return@withContext NetworkCallHandler.Error("user is not authenticate ")

            return@withContext try {
                val fullUrl = Secrets.getUrl() + "/orderItems/statsu"
                val result = client.put(fullUrl) {
                    contentType(ContentType.Application.Json)
                    headers {
                        append(
                            HttpHeaders.Authorization,
                            "Bearer ${authData.refreshToken}"
                        )
                    }
                    setBody(UpdateOrderItemStatusDto(id, status))
                }

                when (result.status) {
                    HttpStatusCode.Companion.NoContent -> {
                        NetworkCallHandler.Successful(true)
                    }

                    else -> {
                        NetworkCallHandler.Error(result.body<String>())
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