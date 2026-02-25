package com.example.eccomerce_app.ui.view.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.savedstate.savedState
import com.example.eccomerce_app.ui.component.Sizer
import com.example.e_commercompose.ui.theme.CustomColor
import com.example.eccomerce_app.ui.component.BannerPage
import com.example.e_commercompose.ui.component.CategoryLoadingShape
import com.example.eccomerce_app.ui.component.CategoryShape
import com.example.e_commercompose.ui.component.ProductLoading
import com.example.eccomerce_app.util.General.reachedBottom
import com.example.e_commercompose.ui.component.BannerLoading
import com.example.eccomerce_app.ui.Screens
import com.example.eccomerce_app.ui.component.HomeAddressComponent
import com.example.eccomerce_app.ui.component.HomeSearchComponent
import com.example.eccomerce_app.ui.component.OpacityAndHideComponent
import com.example.eccomerce_app.ui.component.ProductShape
import com.example.eccomerce_app.viewModel.ProductViewModel
import com.example.eccomerce_app.viewModel.VariantViewModel
import com.example.eccomerce_app.viewModel.BannerViewModel
import com.example.eccomerce_app.viewModel.CategoryViewModel
import com.example.eccomerce_app.viewModel.CurrencyViewModel
import com.example.eccomerce_app.viewModel.GeneralSettingViewModel
import com.example.eccomerce_app.viewModel.HomeViewModel
import com.example.eccomerce_app.viewModel.OrderViewModel
import com.example.eccomerce_app.viewModel.PaymentTypeViewModel
import com.example.eccomerce_app.viewModel.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.collections.isNullOrEmpty

@OptIn(
    ExperimentalMaterial3Api::class
)
@SuppressLint("ConfigurationScreenWidthHeight", "SuspiciousIndentation")
@Composable
fun HomePage(
    nav: NavHostController,
    bannerViewModel: BannerViewModel,
    categoryViewModel: CategoryViewModel,
    variantViewModel: VariantViewModel,
    productViewModel: ProductViewModel,
    userViewModel: UserViewModel,
    generalSettingViewModel: GeneralSettingViewModel,
    orderViewModel: OrderViewModel,
    homeViewModel: HomeViewModel,
    currencyViewModel: CurrencyViewModel,
    paymentTypeViewModel: PaymentTypeViewModel,
    ) {
    val configuration = LocalConfiguration.current

    val layoutDirection = LocalLayoutDirection.current



    val lazyState = rememberLazyGridState()
    val state = rememberPullToRefreshState()
    val coroutine = rememberCoroutineScope()



    val myInfo = userViewModel.userInfo.collectAsStateWithLifecycle()
    val banner = bannerViewModel.bannersRadom.collectAsStateWithLifecycle()
    val categories = categoryViewModel.categories.collectAsStateWithLifecycle()
    val products = productViewModel.products.collectAsStateWithLifecycle()
    val accessHomeScreenCounter =
        homeViewModel.accessHomeScreenCounter.collectAsStateWithLifecycle()
    val paymentTypePageNumb = paymentTypeViewModel.pageNum.collectAsStateWithLifecycle()

    val isClickingSearch = remember { mutableStateOf(false) }
    val isLoadingMore = remember { mutableStateOf(false) }
    val isRefresh = remember { mutableStateOf(false) }
    val isFirst = savedState { derivedStateOf { accessHomeScreenCounter.value == 1 } }
    val reachedBottom = remember { derivedStateOf { lazyState.reachedBottom() } }


    val page = remember { mutableIntStateOf(1) }


    val requestPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { permission ->
        }
    )


    fun initialDataLoad(showRefreshIndicator: Boolean = false) {
        homeViewModel.increaseAccessHomeScreenCounter()
        if (showRefreshIndicator)
            isRefresh.value = true
        coroutine.launch {
            userViewModel.getMyInfo()
            generalSettingViewModel.getGeneral(1)
            categoryViewModel.getCategories(1)
            bannerViewModel.getStoresBanner()
            variantViewModel.getVariants(1)
            productViewModel.getProducts(1) // Pass the reset page
            orderViewModel.getMyOrders(mutableIntStateOf(1)) // Ensure page is managed here too if paginated
            currencyViewModel.getCurrencies(1)
            if (showRefreshIndicator) {
                delay(1000)
                isRefresh.value = false
            }
            paymentTypeViewModel.getPaymentType(paymentTypePageNumb.value)
        }
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermission.launch(input = Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    LaunchedEffect(isFirst) {
        initialDataLoad()
    }


    LaunchedEffect(reachedBottom.value) {
        if (!products.value.isNullOrEmpty() && reachedBottom.value && products.value!!.size > 23) {
            productViewModel.getProducts(
                page.intValue,
                isLoadingMore.value,
                updatePageNumber = { value -> page.intValue = value },
                updateLoadingState = { value -> isLoadingMore.value = value }
            )
        }

    }



    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    )
    { contentPadding ->
        contentPadding.calculateTopPadding()
        contentPadding.calculateBottomPadding()


        val sizeAnimation = animateDpAsState(if (!isClickingSearch.value) 80.dp else 0.dp)



        PullToRefreshBox(
            isRefreshing = isRefresh.value,
            onRefresh = {
                initialDataLoad(true)
            },
            state = state,
            indicator = {
                Indicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    isRefreshing = isRefresh.value,
                    containerColor = Color.White,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    state = state
                )
            },
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(
                    start = 15.dp + contentPadding.calculateLeftPadding(layoutDirection),
                    end = 15.dp + contentPadding.calculateRightPadding(layoutDirection),
                    top = 5.dp + contentPadding.calculateTopPadding(),
                    bottom = 5.dp + contentPadding.calculateBottomPadding()
                )
        )
        {
            LazyVerticalGrid(
                state = lazyState,
                modifier = Modifier
                    .fillMaxSize(),
                columns = GridCells.Adaptive(150.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),

                ) {

                item(span = { GridItemSpan(maxLineSpan) }) {
                    HomeAddressComponent(
                        isPassCondition = myInfo.value?.address == null && categories == null,
                        screenWidth = configuration.screenWidthDp,
                        animatedComponentSize = sizeAnimation.value,
                        onPressDo = {
                            nav.navigate(Screens.EditeOrAddNewAddress)
                        },
                        address = myInfo.value?.address?.firstOrNull { it.isCurrent }
                    )

                }

                //this the search box
                if (!products.value.isNullOrEmpty())
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        HomeSearchComponent(
                            isClickingSearch = isClickingSearch.value,
                            contentPadding = contentPadding
                        ) { state ->
                            isClickingSearch.value = state
                        }
                    }


                item(span = { GridItemSpan(maxLineSpan) }) {

                    OpacityAndHideComponent(
                        isHideComponent = isClickingSearch.value,
                        content = {
                            when (categories.value == null) {
                                true -> {
                                    CategoryLoadingShape()
                                }

                                else -> {
                                    when (categories.value!!.isEmpty()) {
                                        true -> {}
                                        else -> {
                                            CategoryShape(
                                                categories = categories.value!!.take(4),
                                                productViewModel = productViewModel,
                                                onPressDo = { id ->
                                                    productViewModel.getProductsByCategoryID(
                                                        pageNumber = 1,
                                                        categoryId = id
                                                    )
                                                    nav.navigate(
                                                        Screens.ProductCategory(
                                                            id.toString()
                                                        )
                                                    )
                                                },
                                                onPressViewAlDo = {
                                                    nav.navigate(Screens.Category)
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        })
                }

                //banner section
                item(span = { GridItemSpan(maxLineSpan) }) {

                    OpacityAndHideComponent(
                        isHideComponent = isClickingSearch.value,
                        content = {
                            when (banner.value == null) {
                                true -> {
                                    BannerLoading()
                                }

                                else -> {
                                    if (banner.value!!.isNotEmpty())
                                        BannerPage(
                                            banners = banner.value!!,
                                            isMe = false,
                                            onPressDo = { id ->
                                                nav.navigate(
                                                    Screens.Store(id.toString())
                                                )
                                            }
                                        )
                                }
                            }
                        })

                }


                //product

                if (products == null)
                    items(50, key = { value -> value }) {
                        OpacityAndHideComponent(
                            isHideComponent = isClickingSearch.value,
                            content = {
                                ProductLoading()
                            })
                    }

                if (!products.value.isNullOrEmpty())
                    items(products.value!!, key = { value -> value.id.toString() }) { product ->
                        OpacityAndHideComponent(
                            isHideComponent = isClickingSearch.value,
                            content = {
                                ProductShape(
                                    product,
                                    onPressDo = { id, isFromHome, isCanNavigateToStore ->
                                        nav.navigate(
                                            Screens.ProductDetails(
                                                id.toString(),
                                                isFromHome = isFromHome,
                                                isCanNavigateToStore = isCanNavigateToStore
                                            )
                                        )
                                    }
                                )
                            })
                    }

                if (isLoadingMore.value) {

                    item {
                        OpacityAndHideComponent(
                            isHideComponent = isClickingSearch.value,
                            content = {
                                Box(
                                    modifier = Modifier
                                        .padding(top = 15.dp)
                                        .fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                )
                                {
                                    CircularProgressIndicator(color = CustomColor.primaryColor700)
                                }
                                Sizer(40)
                            })
                    }
                }

                item {
                    Sizer(5)
                }
            }

        }
    }
}









