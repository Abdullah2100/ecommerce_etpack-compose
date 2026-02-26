package com.example.core.network.repository

import com.example.core.database.Dao.AuthDao
import com.example.core.database.Model.AuthModelEntity
import com.example.core.network.dto.AddressDto
import com.example.core.network.dto.CreateAddressDto
import com.example.core.network.dto.UpdateAddressDto
import com.example.core.network.Secrets
import com.example.core.network.NetworkCallHandler
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.patch
import io.ktor.client.request.post
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

class AddressRepository(private val client: HttpClient, private val authDao: AuthDao) {

    suspend fun getAuthData(): AuthModelEntity? {
        return authDao.getAuthData()
    }

    suspend fun userAddNewAddress(locationData: CreateAddressDto): NetworkCallHandler =
        withContext(Dispatchers.IO) {
            val authData =
                getAuthData() ?: return@withContext NetworkCallHandler.Error("user is not authenticate ")

            return@withContext try {
                val result = client.post(
                    Secrets.getUrl() + "/User/address"
                ) {
                    headers {
                        append(
                            HttpHeaders.Authorization,
                            "Bearer ${authData.refreshToken}"
                        )
                    }
                    setBody(
                        locationData
                    )
                    contentType(ContentType.Application.Json)

                }

                when (result.status) {
                    HttpStatusCode.Created -> {
                        NetworkCallHandler.Successful(result.body<AddressDto?>())
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

    suspend fun userUpdateAddress(locationData: UpdateAddressDto): NetworkCallHandler =
        withContext(Dispatchers.IO) {
            val authData =
                getAuthData() ?: return@withContext NetworkCallHandler.Error("user is not authenticate ")

            return@withContext try {
                val result = client.put(
                    Secrets.getUrl() + "/User/address"
                ) {
                    headers {
                        append(
                            HttpHeaders.Authorization,
                            "Bearer ${authData.refreshToken}"
                        )
                    }
                    setBody(
                        locationData
                    )
                    contentType(ContentType.Application.Json)

                }

                when (result.status) {
                    HttpStatusCode.Companion.OK -> {
                        NetworkCallHandler.Successful(result.body<AddressDto?>())
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

    suspend fun deleteUserAddress(addressID: UUID): NetworkCallHandler =
        withContext(Dispatchers.IO) {
            val authData =
                getAuthData() ?: return@withContext NetworkCallHandler.Error("user is not authenticate ")

            return@withContext try {
                val result = client.delete(
                    Secrets.getUrl() + "/User/address/${addressID}"
                ) {
                    headers {
                        append(
                            HttpHeaders.Authorization,
                            "Bearer ${authData.refreshToken}"
                        )
                    }

                }

                when (result.status) {
                    HttpStatusCode.Companion.NoContent -> {
                        NetworkCallHandler.Successful(true)
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

    suspend fun setAddressAsCurrent(addressId: UUID): NetworkCallHandler =
        withContext(Dispatchers.IO) {
            val authData =
                getAuthData() ?: return@withContext NetworkCallHandler.Error("user is not authenticate ")

            return@withContext try {
                val result = client.patch(
                    Secrets.getUrl() + "/User/address/active/${addressId}"
                ) {
                    headers {
                        append(
                            HttpHeaders.Authorization,
                            "Bearer ${authData.refreshToken}"
                        )
                    }
                }

                when (result.status) {
                    HttpStatusCode.NoContent -> {
                        NetworkCallHandler.Successful(true)
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

    suspend fun getStoreAddress(id: UUID, pageNumber: Int): NetworkCallHandler =
        withContext(Dispatchers.IO) {
            val authData =
                getAuthData() ?: return@withContext NetworkCallHandler.Error("user is not authenticate ")

            return@withContext try {
                val fullUrl = Secrets.getUrl() + "/Store/${id}/${pageNumber}"
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
                        NetworkCallHandler.Successful(result.body<AddressDto>())
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