package com.example.eccomerce_app.viewModel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eccomerce_app.util.General
import com.example.eccomerce_app.dto.ProductDto
import com.example.eccomerce_app.model.DtoToModel.toProduct
import com.example.e_commercompose.model.ProductModel
import com.example.e_commercompose.model.ProductVarientSelection
import com.example.eccomerce_app.data.NetworkCallHandler
import com.example.eccomerce_app.data.Room.Dao.CurrencyDao
import com.example.eccomerce_app.data.repository.ProductRepository
import com.example.eccomerce_app.util.General.convertPriceToAnotherCurrency
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID


class ProductViewModel(
    private val productRepository: ProductRepository,
    private val currencyDao: CurrencyDao,
    private val scop: CoroutineScope,

//    val webSocket: HubConnection?
) : ViewModel() {


//     val _hub = MutableStateFlow<HubConnection?>(null)

     val _products = MutableStateFlow<List<ProductModel>?>(null)
    val products = _products.asStateFlow()


     val _coroutineException = CoroutineExceptionHandler { _, message ->
        Log.d("ErrorMessageIs", message.message.toString())
    }

    fun setDefaultCurrency(symbol:String, isCompleteUpdate: MutableState<Boolean>) {
        scop.launch(Dispatchers.IO + SupervisorJob()) {
            delay(100)
            currencyDao.setSelectedCurrency(symbol)
             async{convertProductCurrencyToSavedCurrency()}.await()
             isCompleteUpdate.value = false;
        }
    }

    //this if user change the currency from setting
    private suspend fun convertProductCurrencyToSavedCurrency(): Boolean{
        val currencyList = currencyDao.getSavedCurrencies()
        if(!_products.value.isNullOrEmpty()&& currencyList.isNotEmpty()){
            val targetCurrency = currencyList.firstOrNull{it-> it.isSelected }

            val productToNewCurrency = _products.value?.map { data->
                data.copy(
                    price = convertPriceToAnotherCurrency(
                        data.price,
                        data.symbol,
                        targetCurrency,
                        currencyList),
                    symbol = targetCurrency?.symbol?:data.symbol)
            }

            _products.emit(null)
            _products.emit(productToNewCurrency)
            return true

        }
        return false
    }


    //this if the user is select the currency then for api  comming will use this
    // to convert them to local currency saved
    private suspend fun convertProductCurrencyToSavedCurrency(products:List<ProductModel>?=null): List<ProductModel>?{
        val currencyList = currencyDao.getSavedCurrencies()
        val targetCurrency = currencyList.firstOrNull{it-> it.isSelected }
        if(!products.isNullOrEmpty()&&targetCurrency!=null){

            val productToNewCurrency = products.map { data->

                data.copy(price = convertPriceToAnotherCurrency(
                    data.price,
                    data.symbol,
                    targetCurrency,
                    currencyList),
                    symbol = targetCurrency.symbol)

            }

            return productToNewCurrency

        }
        return null
    }



    /*
    fun connection() {

        if (webSocket != null) {
            viewModelScope.launch(Dispatchers.IO + _coroutineException) {

                _hub.emit(webSocket)
                _hub.value?.on(
                    "storeStatus",
                    { result ->

                        viewModelScope.launch(Dispatchers.IO + _coroutineException) {
                            if (result.Status) {
                                val productNotBelongToStore =
                                    _products.value?.filter { it.storeId != result.StoreId }
                                _products.emit(productNotBelongToStore)
                            }
                        }
                    },
                    StoreStatusDto::class.java
                )

            }

        }
    }


    init {

        connection()
    }

    override fun onCleared() {
        viewModelScope.launch(Dispatchers.IO + _coroutineException) {
            if (_hub.value != null)
                _hub.value!!.stop()
        }
        super.onCleared()
    }
*/
    fun getProducts(
        pageNumber: MutableState<Int>,
        isLoading: MutableState<Boolean>? = null
    ) {
        viewModelScope.launch(Dispatchers.IO + _coroutineException) {
            if (isLoading != null) isLoading.value = true
            delay(500)

            val result = productRepository.getProduct(pageNumber.value)
            when (result) {
                is NetworkCallHandler.Successful<*> -> {
                    val data = result.data as List<ProductDto>

                    val holder = mutableListOf<ProductModel>()
                    val productsResponse = data.map { it.toProduct() }.toList()

                    val productWithSavedCurrency= convertProductCurrencyToSavedCurrency(productsResponse)?:productsResponse

                    holder.addAll(productWithSavedCurrency)

                    if (_products.value != null) {
                        holder.addAll(_products.value!!)
                    }

                    val distinctSubCategories = holder.distinctBy { it.id }.toMutableList()

                    if (distinctSubCategories.isNotEmpty())
                        _products.emit(distinctSubCategories)
                    else if (_products.value == null)
                        _products.emit(emptyList())



                    if (isLoading != null) isLoading.value = false
                    if (data.size == 25)
                        pageNumber.value++
                }

                is NetworkCallHandler.Error -> {
                    if (_products.value == null)
                        _products.emit(emptyList())

                    val errorMessage = (result.data.toString())
                    if (errorMessage.contains(General.BASED_URL)) {
                        errorMessage.replace(General.BASED_URL, " Server ")
                    }
                    Log.d("errorFromGettingStoreData", errorMessage)

                    if (isLoading != null) isLoading.value = false
                }
            }

        }

    }

    fun getProducts(
        pageNumber: MutableState<Int>,
        storeId: UUID,
        isLoading: MutableState<Boolean>? = null
    ) {
        if (isLoading != null) isLoading.value = true
        viewModelScope.launch(Dispatchers.Main + _coroutineException) {
            val result = productRepository.getProduct(storeId, pageNumber.value)
            when (result) {
                is NetworkCallHandler.Successful<*> -> {
                    val data = result.data as List<ProductDto>

                    val holder = mutableListOf<ProductModel>()
                    val productsResponse = data.map { it.toProduct() }.toList()

                    val productWithSavedCurrency= convertProductCurrencyToSavedCurrency(productsResponse)?:productsResponse

                    holder.addAll(productWithSavedCurrency)
                    if (_products.value != null) {
                        holder.addAll(_products.value!!)
                    }

                    val distinctSubCategories = holder.distinctBy { it.id }.toMutableList()

                    if (distinctSubCategories.isNotEmpty())
                        _products.emit(distinctSubCategories)
                    else if (_products.value == null)
                        _products.emit(emptyList())
                    if (isLoading != null) isLoading.value = false
                    if (data.size == 25)
                        pageNumber.value++

                }

                is NetworkCallHandler.Error -> {
                    if (_products.value == null)
                        _products.emit(emptyList())

                    val errorMessage = (result.data.toString())
                    if (errorMessage.contains(General.BASED_URL)) {
                        errorMessage.replace(General.BASED_URL, " Server ")
                    }
                    Log.d("errorFromGettingStoreData", errorMessage)
                    if (isLoading != null) isLoading.value = false
                }
            }
        }

    }

    fun getProductsByCategoryID(
        pageNumber: MutableState<Int>,
        categoryId: UUID,
        isLoading: MutableState<Boolean>? = null
    ) {
        if (isLoading != null) isLoading.value = true
        viewModelScope.launch(Dispatchers.Main + _coroutineException) {
            val result = productRepository
                .getProductByCategoryId(
                    categoryId,
                    pageNumber.value
                )
            when (result) {
                is NetworkCallHandler.Successful<*> -> {
                    val data = result.data as List<ProductDto>

                    val holder = mutableListOf<ProductModel>()

                    val productsResponse = data.map { it.toProduct() }.toList()

                    val productWithSavedCurrency= convertProductCurrencyToSavedCurrency(productsResponse)?:productsResponse

                    holder.addAll(productWithSavedCurrency)

                    if (_products.value != null) {
                        holder.addAll(_products.value!!)
                    }

                    val distinctSubCategories = holder.distinctBy { it.id }.toMutableList()

                    if (distinctSubCategories.isNotEmpty())
                        _products.emit(distinctSubCategories)
                    else if (_products.value == null)
                        _products.emit(emptyList())
                    if (isLoading != null) isLoading.value = false
                    if (data.size == 25)
                        pageNumber.value++

                }

                is NetworkCallHandler.Error -> {
                    if (_products.value == null)
                        _products.emit(emptyList())

                    val errorMessage = (result.data.toString())
                    if (errorMessage.contains(General.BASED_URL)) {
                        errorMessage.replace(General.BASED_URL, " Server ")
                    }
                    Log.d("errorFromGettingStoreData", errorMessage)
                    if (isLoading != null) isLoading.value = false
                }
            }
        }

    }


    fun getProducts(
        pageNumber: MutableState<Int>,
        storeId: UUID,
        subcategoryId: UUID,
        isLoading: MutableState<Boolean>? = null
    ) {
        if (isLoading != null) isLoading.value = true
        viewModelScope.launch(Dispatchers.Main + _coroutineException) {
            val result = productRepository.getProduct(
                storeId,
                subcategoryId,
                pageNumber = pageNumber.value
            )
            when (result) {
                is NetworkCallHandler.Successful<*> -> {
                    val data = result.data as List<ProductDto>

                    val holder = mutableListOf<ProductModel>()
                    val productsResponse = data.map { it.toProduct() }.toList()

                    val productWithSavedCurrency= convertProductCurrencyToSavedCurrency(productsResponse)?:productsResponse

                    holder.addAll(productWithSavedCurrency)


                    if (_products.value != null) {
                        holder.addAll(_products.value!!)
                    }

                    val distinticSubCategories = holder.distinctBy { it.id }.toMutableList()

                    if (distinticSubCategories.size > 0)
                        _products.emit(distinticSubCategories)
                    else if (_products.value == null)
                        _products.emit(emptyList())

                    if (isLoading != null) isLoading.value = false
                    if (data.size == 25)
                        pageNumber.value++

                }

                is NetworkCallHandler.Error -> {
                    if (_products.value == null)
                        _products.emit(emptyList())

                    val errorMessage = (result.data.toString())
                    if (errorMessage.contains(General.BASED_URL)) {
                        errorMessage.replace(General.BASED_URL, " Server ")
                    }
                    Log.d("errorFromGettingStoreData", errorMessage)
                    if (isLoading != null) isLoading.value = false

                }
            }
        }

    }

    suspend fun createProducts(
        name: String,
        description: String,
        thmbnail: File,
        subcategoryId: UUID,
        storeId: UUID,
        price: Double,
        symbol:String,
        productVariants: List<ProductVarientSelection>,
        images: List<File>
    ): String? {
        val result = productRepository.createProduct(
            name,
            description,
            thmbnail,
            subcategoryId,
            storeId,
            price,
            symbol,
            productVariants,
            images
        )
        when (result) {
            is NetworkCallHandler.Successful<*> -> {
                val data = result.data as ProductDto

                val holder = mutableListOf<ProductModel>()
                val addressResponse = data.toProduct()

                holder.add(addressResponse)
                if (_products.value != null) {
                    holder.addAll(_products.value!!)
                }

                if (holder.isNotEmpty())
                    _products.emit(holder)
                else
                    _products.emit(emptyList())
                return null
            }

            is NetworkCallHandler.Error -> {
                _products.emit(emptyList())

                val errorMessage = (result.data.toString())
                if (errorMessage.contains(General.BASED_URL)) {
                    errorMessage.replace(General.BASED_URL, " Server ")
                }
                Log.d("errorFromGettingStoreData", errorMessage)
                return errorMessage.replace("\"", "")
            }
        }
    }

    suspend fun updateProducts(
        id: UUID,
        name: String?,
        description: String?,
        thmbnail: File?,
        subcategoryId: UUID?,
        storeId: UUID,
        price: Double?,
        symbol:String?,
        productVariants: List<ProductVarientSelection>?,
        images: List<File>?,
        deletedProductVariants: List<ProductVarientSelection>?,
        deletedImages: List<String>?

    ): String? {
        val result = productRepository.updateProduct(
            id,
            name,
            description,
            thmbnail,
            subcategoryId,
            storeId,
            price,
            symbol,
            productVariants,
            images,
            deletedProductVariants,
            deletedImages
        )
        when (result) {
            is NetworkCallHandler.Successful<*> -> {
                val data = result.data as ProductDto

                val holder = mutableListOf<ProductModel>()
                val addressResponse = data.toProduct()

                holder.add(addressResponse)
                if (_products.value != null) {
                    holder.addAll(_products.value!!)
                }
                val distnectHolder = holder.distinctBy { it.id }

                if (distnectHolder.size > 0)
                    _products.emit(distnectHolder)

                return null
            }

            is NetworkCallHandler.Error -> {
                _products.emit(emptyList())

                val errorMessage = (result.data.toString())
                if (errorMessage.contains(General.BASED_URL)) {
                    errorMessage.replace(General.BASED_URL, " Server ")
                }
                Log.d("errorFromGettingStoreData", errorMessage)
                return errorMessage.replace("\"", "")
            }
        }
    }


    suspend fun deleteProduct(storeId: UUID, productId: UUID): String? {
        val result = productRepository.deleteProduct(
            storeId = storeId,
            productId = productId
        )
        when (result) {
            is NetworkCallHandler.Successful<*> -> {
                if (products.value != null) {
                    val copyProduct = _products.value?.filter { it.id != productId }
                    _products.emit(copyProduct)
                }
                return null
            }

            is NetworkCallHandler.Error -> {

                val errorMessage = (result.data.toString())
                if (errorMessage.contains(General.BASED_URL)) {
                    errorMessage.replace(General.BASED_URL, " Server ")
                }
                Log.d("errorFromGettingStoreData", errorMessage)
                return errorMessage.replace("\"", "")
            }
        }
    }


}

