package com.example.eccomerce_app.viewModel

import androidx.lifecycle.ViewModel
import com.example.eccomerce_app.util.General
import com.example.core.network.NetworkCallHandler
import com.example.core.database.Dao.CurrencyDao
import com.example.core.database.Model.Currency
import com.example.core.network.Secrets
import com.example.core.network.repository.CurrencyRepository
import com.example.core.network.dto.CurrencyDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class CurrencyViewModel(
    private val currencyRepository: CurrencyRepository,
    private val scop: CoroutineScope,
    private val currencyDao: CurrencyDao
) : ViewModel() {


    val selectedCurrency = currencyDao
        .getSelectedCurrencyFlow()
        .stateIn(
            scop,
            started = SharingStarted.WhileSubscribed(2000L),
            initialValue = null
        )

    val currenciesList = currencyDao
        .getSavedCurrenciesAsFlow()
        .stateIn(
            scop,
            started = SharingStarted.WhileSubscribed(2000L),
            initialValue = null
        )




    fun getCurrencies(pageNumber: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val result = currencyRepository.getStoreCurrencies(pageNumber = pageNumber)) {
                is NetworkCallHandler.Successful<*> -> {
                    val data = result.data as List<CurrencyDto>

                    if (data.isEmpty()) {
                        return@launch
                    }

                    val selectedCurrency = currencyDao.getSelectedCurrency();
                    currencyDao.deleteCurrencies()
                    val currencyToDbModel = data.map { data ->
                        Currency(
                            symbol = data.symbol,
                            name = data.name,
                            value = data.value,
                            isDefault = data.isDefault,
                            isSelected = selectedCurrency?.name == data.name,
                            id = null
                        )
                    }
                    currencyDao.addNewCurrency(currencyToDbModel)

                }

                is NetworkCallHandler.Error -> {
                    val errorMessage = (result.data.toString())
                    if (errorMessage.contains(Secrets.getUrl())) {
                        errorMessage.replace(Secrets.getUrl(), " Server ")
                    }
                }
            }
        }

    }

}