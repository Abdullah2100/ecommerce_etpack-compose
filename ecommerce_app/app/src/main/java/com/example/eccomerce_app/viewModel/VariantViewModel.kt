package com.example.eccomerce_app.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.model.VariantModel
import com.example.core.network.NetworkCallHandler
import com.example.core.network.dto.VariantDto
import com.example.eccomerce_app.model.DtoToModel.toVariant
import com.example.core.network.repository.VariantRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class VariantViewModel(val variantRepository: VariantRepository) : ViewModel() {


  private   val _variants = MutableStateFlow<MutableList<VariantModel>?>(null)
    val variants = _variants.asStateFlow()

  private   val _coroutineException = CoroutineExceptionHandler { _, message ->
        Log.d("ErrorMessageIs", message.message.toString())
    }

    fun getVariants(pageNumber: Int = 1) {
        if (pageNumber == 1 && _variants.value != null) return
        viewModelScope.launch(_coroutineException) {

            when (val result = variantRepository.getVariant(pageNumber)) {
                is NetworkCallHandler.Successful<*> -> {
                    val variantsHolder = result.data as List<VariantDto>

                    val mutableVariant = mutableListOf<VariantModel>()

                    if (pageNumber != 1 && _variants.value != null) {
                        mutableVariant.addAll(_variants.value!!.toList())
                    }
                    if (variantsHolder.isNotEmpty()) mutableVariant.addAll(
                        variantsHolder.map { it.toVariant() }.toList()
                    )

                    if (mutableVariant.isNotEmpty()) {
                        _variants.emit(
                            mutableVariant
                        )
                    } else {
                        if (_variants.value == null) _variants.emit(mutableListOf())
                    }
                }

                else -> {}
            }
        }
    }

    init {
        getVariants(1)
    }
}
