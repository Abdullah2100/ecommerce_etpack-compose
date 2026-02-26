package com.example.core.network.repository

import com.example.core.database.Dao.AuthDao
import com.example.core.database.Model.AuthModelEntity
import com.example.core.network.dto.CategoryDto
import com.example.core.network.Secrets
import com.example.core.network.NetworkCallHandler
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.UnknownHostException

class CategoryRepository(private val client: HttpClient, private val authDao: AuthDao) {

    suspend fun getAuthData(): AuthModelEntity? {
        return authDao.getAuthData()
    }

    suspend fun getCategory(pageNumber: Int): NetworkCallHandler = withContext(Dispatchers.IO) {
        val authData =
            getAuthData() ?: return@withContext NetworkCallHandler.Error("user is not authenticate ")

        return@withContext try {
            val result = client.get(
                Secrets.getUrl() + "/Category/all/${pageNumber}"
            ) {
                headers {
                    append(
                        HttpHeaders.Authorization,
                        "Bearer ${authData.refreshToken}"
                    )
                }
            }
            when (result.status) {
                HttpStatusCode.Companion.OK -> {
                    NetworkCallHandler.Successful(result.body<List<CategoryDto>>())
                }

                HttpStatusCode.Companion.NoContent -> {
                    NetworkCallHandler.Error("No Data Found")

                }

                else -> {
                    NetworkCallHandler.Error(result.body())
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