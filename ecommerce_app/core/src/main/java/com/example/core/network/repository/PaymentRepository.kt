package com.example.core.network.repository

import com.example.core.database.Model.AuthModelEntity
import com.example.core.network.dto.StripeClientSecret
import com.example.core.network.dto.StripeDtoRequest
import com.example.core.network.Secrets
import com.example.core.network.NetworkCallHandler
import com.example.core.database.Dao.AuthDao
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.UnknownHostException

class PaymentRepository(private val client: HttpClient, private val authDao: AuthDao) {

    suspend fun getAuthData(): AuthModelEntity? {
        return authDao.getAuthData()
    }

    suspend fun generatePaymentIntent(totalPrice: Long): NetworkCallHandler =
        withContext(Dispatchers.IO) {
            val authData =
                getAuthData() ?: return@withContext NetworkCallHandler.Error("user is not authenticate ")

            return@withContext try {
                val url = Secrets.getUrl() + "/payment/createCheckout"
                val response = client.post(url) {
                    headers {
                        append(
                            HttpHeaders.Authorization,
                            "Bearer ${authData.refreshToken}"
                        )
                    }

                    contentType(ContentType.Application.Json)

                    setBody(StripeDtoRequest(totalPrice, "usd"))
                }

                if (response.status == HttpStatusCode.OK) {
                    NetworkCallHandler.Successful(response.body<StripeClientSecret>())
                } else {
                    NetworkCallHandler.Error(response.body())
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