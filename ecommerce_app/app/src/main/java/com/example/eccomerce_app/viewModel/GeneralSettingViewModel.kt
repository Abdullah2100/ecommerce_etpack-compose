package com.example.eccomerce_app.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eccomerce_app.model.DtoToModel.toGeneralSetting
import com.example.common.model.GeneralSetting
import com.example.core.network.dto.GeneralSettingDto
import com.example.core.network.NetworkCallHandler
import com.example.core.network.repository.GeneralSettingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GeneralSettingViewModel(
    val generalSettingRepository: GeneralSettingRepository
) : ViewModel() {

   private  val _generalSetting = MutableStateFlow<List<GeneralSetting>?>(null)
    val generalSetting = _generalSetting.asStateFlow()


    fun getGeneral(pageNumber: Int = 1) {
        viewModelScope.launch {
            when (val result = generalSettingRepository.getGeneral(pageNumber)) {
                is NetworkCallHandler.Successful<*> -> {
                    val generalSettings = result.data as List<GeneralSettingDto>

                    val mutableGeneralSetting = mutableListOf<GeneralSetting>()

                    mutableGeneralSetting.addAll(generalSettings.map { it.toGeneralSetting() })

                    if (_generalSetting.value != null) {
                        mutableGeneralSetting.addAll(_generalSetting.value!!.toList())
                    }

                    if (mutableGeneralSetting.isNotEmpty()) {
                        _generalSetting.emit(
                            mutableGeneralSetting.distinctBy { it.name }.toList()
                        )
                    }

                }

                is NetworkCallHandler.Error -> {
                    Log.d("GeneralSettingError", result.data.toString())
                }
            }
        }
    }


}