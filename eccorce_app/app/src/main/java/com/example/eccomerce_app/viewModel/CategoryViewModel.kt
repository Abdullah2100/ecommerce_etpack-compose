package com.example.eccomerce_app.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commercompose.model.Category
import com.example.eccomerce_app.model.DtoToModel.toCategory
import com.example.eccomerce_app.dto.CategoryDto
import com.example.eccomerce_app.data.NetworkCallHandler
import com.example.eccomerce_app.data.repository.CategoryRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class CategoryViewModel(val categoryRepository: CategoryRepository) : ViewModel() {
      private  val _categories = MutableStateFlow<MutableList<Category>?>(null)
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
//                        _categories.emit(
//                            mutableCategories
//                        )
                        //this to doublicate the category  from api
                        _categories.emit(List(8){ Category(
                            id = UUID.randomUUID(),
                            image =mutableCategories.get(0).image,
                            name =mutableCategories.get(0).name
                        )}.toMutableList())
                    }
                    if(_categories.value.isNullOrEmpty())
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
