package com.example.eccomerce_app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.model.Category
import com.example.core.network.dto.CategoryDto
import com.example.core.network.NetworkCallHandler
import com.example.core.network.repository.CategoryRepository
import com.example.eccomerce_app.model.DtoToModel.toCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class CategoryViewModel(val categoryRepository: CategoryRepository) : ViewModel() {
    private val _categories = MutableStateFlow<MutableList<Category>?>(null)
    val categories = _categories.asStateFlow()


    fun getCategories(pageNumber: Int = 1) {
        if (pageNumber == 1 && !_categories.value.isNullOrEmpty()) return
        viewModelScope.launch {
            when (val result = categoryRepository.getCategory(pageNumber)) {
                is NetworkCallHandler.Successful<*> -> {
                    val categoriesHolder = result.data as List<CategoryDto>

                    val mutableCategories = mutableListOf<Category>()

                    if (pageNumber != 1 && _categories.value != null) {
                        mutableCategories.addAll(_categories.value!!.toList())
                    }
                    if (categoriesHolder.isNotEmpty())
                        mutableCategories.addAll(
                            categoriesHolder.map { it.toCategory() }.toList()
                        )

                    if (mutableCategories.isNotEmpty()) {
                        _categories.emit(
                            mutableCategories
                        )
                    }
                    if (_categories.value.isNullOrEmpty())
                        _categories.emit(mutableListOf())

                }

                is NetworkCallHandler.Error -> {
                    if (_categories.value == null)
                        _categories.emit(mutableListOf())
                }
            }
        }
    }


    init {
        getCategories(1)
    }

}
