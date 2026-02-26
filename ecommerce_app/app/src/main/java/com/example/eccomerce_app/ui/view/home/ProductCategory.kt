package com.example.eccomerce_app.ui.view.home

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.retain.retain
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.eccomerce_app.ui.component.Sizer
import com.example.e_commercompose.ui.theme.CustomColor
import com.example.e_commercompose.ui.component.ProductLoading
import com.example.eccomerce_app.ui.Screens
import com.example.eccomerce_app.ui.component.ProductShape
import com.example.eccomerce_app.ui.component.SharedAppBar
import com.example.eccomerce_app.ui.view.account.store.ProductDetail
import com.example.eccomerce_app.util.General.reachedBottom
import com.example.eccomerce_app.viewModel.ProductViewModel
import com.example.eccomerce_app.viewModel.CategoryViewModel
import com.example.eccomerce_app.viewModel.CartViewModel
import com.example.eccomerce_app.viewModel.BannerViewModel
import com.example.eccomerce_app.viewModel.CurrencyViewModel
import com.example.eccomerce_app.viewModel.StoreViewModel
import com.example.eccomerce_app.viewModel.SubCategoryViewModel
import com.example.eccomerce_app.viewModel.UserViewModel
import com.example.eccomerce_app.viewModel.VariantViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID


@OptIn(
    ExperimentalMaterial3Api::class
)
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun ProductCategoryScreen(
    nav: NavHostController? = null,
    categoryId: String,
    categoryViewModel: CategoryViewModel,
    productViewModel: ProductViewModel,
) {
    val context = LocalContext.current
    val activity = context as Activity


    val categories = categoryViewModel.categories.collectAsStateWithLifecycle()
    val products = productViewModel.products.collectAsStateWithLifecycle()

    val categoryId = UUID.fromString(categoryId)
    val productsByCategory = products.value?.filter { it.categoryId == categoryId }

    val coroutine = rememberCoroutineScope()
    val lazyState = rememberLazyGridState()
    val state = rememberPullToRefreshState()

    val layoutDirection = LocalLayoutDirection.current

    val reachedBottom = remember { derivedStateOf { lazyState.reachedBottom() } }
    val isLoadingMore = remember { mutableStateOf(false) }
    val isRefresh = remember { mutableStateOf(false) }

    val page = remember { mutableIntStateOf(1) }

    LaunchedEffect(reachedBottom.value) {
        if (!productsByCategory.isNullOrEmpty() && reachedBottom.value && productsByCategory.size > 23) {
            productViewModel.getProductsByCategoryID(
                page.intValue,
                categoryId,
                isLoadingMore.value,
                updateLoadingState = { value -> isLoadingMore.value = value },
                updatePageNumber = { value -> page.intValue = value }
            )
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        topBar = {
            SharedAppBar(
                title = categories.value?.firstOrNull { it.id == categoryId }?.name ?: "",
                nav = nav,
            )
        }
    )
    { contentPadding ->
        contentPadding.calculateTopPadding()
        contentPadding.calculateBottomPadding()

        PullToRefreshBox(
            isRefreshing = isRefresh.value,
            onRefresh = {
                coroutine.launch {
                    if (!isRefresh.value) isRefresh.value = true
                    page.intValue = 1;
                    productViewModel.getProductsByCategoryID(
                        1,
                        categoryId,
                        isLoadingMore.value,
                        updatePageNumber = { page.intValue = 1 },
                        updateLoadingState = { value -> isLoadingMore.value = value }
                    )
                    if (isRefresh.value) {
                        delay(1000)
                        isRefresh.value = false
                    }
                }
            },
            state = state,
            indicator = {
                Indicator(
                    modifier = Modifier
                        .padding(top = contentPadding.calculateTopPadding())
                        .align(Alignment.TopCenter),
                    isRefreshing = isRefresh.value,
                    containerColor = Color.White,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    state = state
                )
            },
        ) {
            LazyVerticalGrid(
                state = lazyState,
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxSize()
                    .padding(
                        start = 5.dp + contentPadding.calculateLeftPadding(layoutDirection),
                        end = 5.dp + contentPadding.calculateRightPadding(layoutDirection),
                        top = 5.dp + contentPadding.calculateTopPadding(),
                        bottom = 5.dp + contentPadding.calculateBottomPadding()
                    )
                ,

                columns = GridCells.Adaptive(150.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                if (productsByCategory == null)
                    items(50, key = { value -> value }) {
                        ProductLoading()
                    }
                if (!productsByCategory.isNullOrEmpty())
                    items(productsByCategory, key = { value -> value.id.toString() }) { product ->

                        ProductShape(product, onPressDo = { id, isFromHome, isCanNavToStore ->
                            nav?.navigate(
                                Screens.ProductDetails(
                                    id.toString(),
                                    isFromHome = isFromHome,
                                    isCanNavigateToStore = isCanNavToStore
                                )
                            )
                        })
                    }

                if (isLoadingMore.value) {
                    item {
                        Box(
                            modifier = Modifier
                                .padding(top = 15.dp)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = CustomColor.primaryColor700)
                        }
                    }
                }

                item {
                    Sizer(40)
                }

            }
        }
    }
}