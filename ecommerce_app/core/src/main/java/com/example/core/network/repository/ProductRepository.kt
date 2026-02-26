package com.example.core.network.repository

import com.example.common.model.ProductVariantSelection
import com.example.core.database.Model.AuthModelEntity
import com.example.core.network.dto.ProductDto
import com.example.core.network.Secrets
import com.example.core.network.NetworkCallHandler
import com.example.core.database.Dao.AuthDao
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.net.UnknownHostException
import java.util.UUID

class ProductRepository(private val client: HttpClient, private val authDao: AuthDao) {

    suspend fun getAuthData(): AuthModelEntity? {
        return authDao.getAuthData()
    }

    suspend fun getProduct(pageNumber: Int): NetworkCallHandler = withContext(Dispatchers.IO) {
        val authData =
            getAuthData() ?: return@withContext NetworkCallHandler.Error("user is not authenticate ")

        return@withContext try {
            val fullUrl = Secrets.getUrl() + "/Product/all/${pageNumber}"
            val result = client.get(fullUrl) {
                headers {
                    append(
                        HttpHeaders.Authorization,
                        "Bearer ${authData.refreshToken}"
                    )
                }
            }

            when (result.status) {
                HttpStatusCode.OK -> {
                    NetworkCallHandler.Successful(result.body<List<ProductDto>>())
                }

                HttpStatusCode.NoContent -> {
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

    suspend fun getProductByCategoryId(
        categoryId: UUID,
        pageNumber: Int
    ): NetworkCallHandler = withContext(Dispatchers.IO) {
        val authData =
            getAuthData() ?: return@withContext NetworkCallHandler.Error("user is not authenticate ")

        return@withContext try {
            val fullUrl = Secrets.getUrl() + "/Product/category/${categoryId}/${pageNumber}"
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
                    NetworkCallHandler.Successful(result.body<List<ProductDto>>())
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

    suspend fun getProduct(storeId: UUID, pageNumber: Int): NetworkCallHandler =
        withContext(Dispatchers.IO) {
            val authData =
                getAuthData() ?: return@withContext NetworkCallHandler.Error("user is not authenticate ")

            return@withContext try {
                val fullUrl = Secrets.getUrl() + "/Product/${storeId}/${pageNumber}"
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
                        NetworkCallHandler.Successful(result.body<List<ProductDto>>())
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

    suspend fun getProduct(
        storeId: UUID,
        subCategory: UUID,
        pageNumber: Int
    ): NetworkCallHandler = withContext(Dispatchers.IO) {
        val authData =
            getAuthData() ?: return@withContext NetworkCallHandler.Error("user is not authenticate ")

        return@withContext try {
            val fullUrl =
                Secrets.getUrl() + "/Product/${storeId}/${subCategory}/${pageNumber}"
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
                    NetworkCallHandler.Successful(result.body<List<ProductDto>>())
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


    suspend fun createProduct(
        name: String,
        description: String,
        thumbnail: File,
        subcategoryId: UUID,
        storeId: UUID,
        price: Int,
        symbol: String,
        productVariants: List<ProductVariantSelection>,
        images: List<File>
    ): NetworkCallHandler = withContext(Dispatchers.IO) {
        val authData =
            getAuthData() ?: return@withContext NetworkCallHandler.Error("user is not authenticate ")

        return@withContext try {
            val fullUrl = Secrets.getUrl() + "/Product"
            val result = client.post(fullUrl) {
                headers {
                    append(
                        HttpHeaders.Authorization,
                        "Bearer ${authData.refreshToken}"
                    )
                }
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append("name", name)
                            append("description", description)
                            append(
                                key = "Thumbnail", // Must match backend expectation
                                value = thumbnail.readBytes(),
                                headers = Headers.Companion.build {
                                    append(
                                        HttpHeaders.ContentType,
                                        "image/${thumbnail.extension}"
                                    )
                                    append(
                                        HttpHeaders.ContentDisposition,
                                        "filename=${thumbnail.name}"
                                    )
                                }
                            )

                            append("SubcategoryId", subcategoryId.toString())
                            append("StoreId", storeId.toString())
                            append("Price", price)
                            append("symbol", symbol)
                            if (productVariants.isNotEmpty())
                                productVariants.forEachIndexed { index, value ->
                                    append("productVarients[${index}].name", value.name)
                                    append("productVarients[${index}].percentage", value.percentage!!)
                                    append(
                                        "productVarients[${index}].valientId",
                                        value.variantId.toString()
                                    )
                                }
                            images.forEachIndexed { index, value ->
                                append(
                                    key = "images", // Must match backend expectation
                                    value = value.readBytes(),
                                    headers = Headers.Companion.build {
                                        append(
                                            HttpHeaders.ContentType,
                                            "image/${value.extension}"
                                        )
                                        append(
                                            HttpHeaders.ContentDisposition,
                                            "filename=${value.name}"
                                        )
                                    }
                                )
                            }


                        }
                    )
                )
            }

            when (result.status) {
                HttpStatusCode.Companion.Created -> {
                    NetworkCallHandler.Successful(result.body<ProductDto>())
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

    suspend fun updateProduct(
        id: UUID,
        name: String?,
        description: String?,
        thumbnail: File?,
        subcategoryId: UUID?,
        storeId: UUID,
        price: Int?,
        symbol: String?,
        productVariants: List<ProductVariantSelection>?,
        images: List<File>?,
        deletedProductVariants: List<ProductVariantSelection>?,
        deleteImages: List<String>?
    ): NetworkCallHandler = withContext(Dispatchers.IO) {
        val authData =
            getAuthData() ?: return@withContext NetworkCallHandler.Error("user is not authenticate ")

        return@withContext try {
            val fullUrl = Secrets.getUrl() + "/Product"
            val result = client.put(fullUrl) {
                headers {
                    append(
                        HttpHeaders.Authorization,
                        "Bearer ${authData.refreshToken}"
                    )
                }
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append("id", id.toString())
                            if (name != null)
                                append("Name", name)
                            if (description != null)
                                append("description", description)
                            if (thumbnail != null)
                                append(
                                    key = "Thumbnail", // Must match backend expectation
                                    value = thumbnail.readBytes(),
                                    headers = Headers.Companion.build {
                                        append(
                                            HttpHeaders.ContentType,
                                            "image/${thumbnail.extension}"
                                        )
                                        append(
                                            HttpHeaders.ContentDisposition,
                                            "filename=${thumbnail.name}"
                                        )
                                    }
                                )
                            if (subcategoryId != null)
                                append("SubcategoryId", subcategoryId.toString())
                            append("StoreId", storeId.toString())

                            if (price != null)
                                append("price", price)

                            if (symbol != null)
                                append("symbol", symbol)

                            if (!productVariants.isNullOrEmpty())
                                productVariants.forEachIndexed { index, value ->
                                    append("productVariants[${index}].name", value.name)
                                    append("productVariants[${index}].percentage", value.percentage!!)
                                    append(
                                        "productVariants[${index}].variantId",
                                        value.variantId.toString()
                                    )
                                }
                            if (!deletedProductVariants.isNullOrEmpty())
                                deletedProductVariants.forEachIndexed { index, value ->
                                    append("deletedProductVariants[${index}].name", value.name)
                                    append(
                                        "deletedProductVariants[${index}].percentage",
                                        value.percentage!!
                                    )
                                    append(
                                        "deletedProductVariants[${index}].valientId",
                                        value.variantId.toString()
                                    )

                                }

                            if (!deleteImages.isNullOrEmpty())
                                deleteImages.forEachIndexed { index, value ->
                                    val startIndex = "staticFiles"
                                    val indexAt = value.indexOf("staticFiles")
                                    append(
                                        "deletedimages[${index}]", value.substring(
                                            indexAt + startIndex.length,
                                            value.length
                                        )
                                    )
                                }


                            if (!images.isNullOrEmpty())
                                images.forEachIndexed { index, value ->
                                    append(
                                        key = "Images", // Must match backend expectation
                                        value = value.readBytes(),
                                        headers = Headers.Companion.build {
                                            append(
                                                HttpHeaders.ContentType,
                                                "image/${value.extension}"
                                            )
                                            append(
                                                HttpHeaders.ContentDisposition,
                                                "filename=${value.name}"
                                            )
                                        }
                                    )
                                }


                        }
                    )
                )
            }

            when (result.status) {
                HttpStatusCode.Companion.OK -> {
                    NetworkCallHandler.Successful(result.body<ProductDto>())
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

    suspend fun deleteProduct(storeId: UUID, productId: UUID): NetworkCallHandler =
        withContext(Dispatchers.IO) {
            val authData =
                getAuthData() ?: return@withContext NetworkCallHandler.Error("user is not authenticate ")

            return@withContext try {
                val fullUrl = Secrets.getUrl() + "/Product/${storeId}/${productId}"
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

}