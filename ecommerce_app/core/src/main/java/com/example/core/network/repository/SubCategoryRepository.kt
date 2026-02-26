package com.example.core.network.repository

import com.example.core.database.Model.AuthModelEntity
import com.example.core.network.dto.CreateSubCategoryDto
import com.example.core.network.dto.SubCategoryDto
import com.example.core.network.dto.UpdateSubCategoryDto
import com.example.core.network.Secrets
import com.example.core.network.NetworkCallHandler
import com.example.core.database.Dao.AuthDao
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.headers
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

class SubCategoryRepository(private val client: HttpClient, private val authDao: AuthDao) {

    suspend fun getAuthData(): AuthModelEntity? {
        return authDao.getAuthData()
    }

    suspend fun createSubCategory(data: CreateSubCategoryDto): NetworkCallHandler =
        withContext(Dispatchers.IO) {
            val authData =
                getAuthData() ?: return@withContext NetworkCallHandler.Error("user is not authenticate ")

            return@withContext try {
                val fullUrl = Secrets.getUrl() + "/SubCategory"
                val result = client.post(fullUrl) {
                    headers {
                        append(
                            HttpHeaders.Authorization,
                            "Bearer ${authData.refreshToken}"
                        )
                    }
                    setBody(data)
                    contentType(ContentType.Application.Json)

                }

                when (result.status) {
                    HttpStatusCode.Companion.Created -> {
                        NetworkCallHandler.Successful(result.body<SubCategoryDto>())
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


    suspend fun updateSubCategory(data: UpdateSubCategoryDto): NetworkCallHandler =
        withContext(Dispatchers.IO) {
            val authData =
                getAuthData() ?: return@withContext NetworkCallHandler.Error("user is not authenticate ")

            return@withContext try {
                val fullUrl = Secrets.getUrl() + "/SubCategory"
                val result = client.put(fullUrl) {
                    headers {
                        append(
                            HttpHeaders.Authorization,
                            "Bearer ${authData.refreshToken}"
                        )
                    }
                    setBody(data)
                    contentType(ContentType.Application.Json)

                }

                when (result.status) {
                    HttpStatusCode.Companion.OK -> {
                        NetworkCallHandler.Successful(result.body<SubCategoryDto>())
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


    suspend fun deleteSubCategory(subCategoryID: UUID): NetworkCallHandler =
        withContext(Dispatchers.IO) {
            val authData =
                getAuthData() ?: return@withContext NetworkCallHandler.Error("user is not authenticate ")

            return@withContext try {
                val fullUrl = Secrets.getUrl() + "/SubCategory/${subCategoryID}"
                val result = client.delete(fullUrl) {
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


    suspend fun getStoreSubCategory(id: UUID, pageNumber: Int): NetworkCallHandler =
        withContext(Dispatchers.IO) {
            val authData =
                getAuthData() ?: return@withContext NetworkCallHandler.Error("user is not authenticate ")

            return@withContext try {
                val fullUrl = Secrets.getUrl() + "/SubCategory/${id}/${pageNumber}"
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
                        NetworkCallHandler.Successful(result.body<List<SubCategoryDto>>())
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

}