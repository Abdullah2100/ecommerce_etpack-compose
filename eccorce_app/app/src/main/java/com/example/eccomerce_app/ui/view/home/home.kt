package com.example.eccomerce_app.ui.view.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Configuration
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.savedstate.savedState
import com.example.e_commercompose.model.BannerModel
import com.example.e_commercompose.model.Category
import com.example.e_commercompose.model.ProductModel
import com.example.e_commercompose.model.UserModel
import com.example.eccomerce_app.ui.component.Sizer
import com.example.e_commercompose.ui.theme.CustomColor
import com.example.eccomerce_app.ui.component.BannerPage
import com.example.e_commercompose.ui.component.CategoryLoadingShape
import com.example.eccomerce_app.ui.component.CategoryShape
import com.example.e_commercompose.ui.component.ProductLoading
import com.example.eccomerce_app.ui.component.ProductShape
import com.example.eccomerce_app.util.General.reachedBottom
import com.example.e_commercompose.ui.component.BannerLoading
import com.example.eccomerce_app.ui.Screens
import com.example.eccomerce_app.ui.component.HomeAddressComponent
import com.example.eccomerce_app.ui.component.HomeSearchComponent
import com.example.eccomerce_app.ui.component.OpacityAndHideComponent
import com.example.eccomerce_app.ui.view.account.store.ProductDetail
import com.example.eccomerce_app.ui.view.account.store.StoreScreen
import com.example.eccomerce_app.ui.view.address.EditOrAddLocationScreen
import com.example.eccomerce_app.viewModel.ProductViewModel
import com.example.eccomerce_app.viewModel.VariantViewModel
import com.example.eccomerce_app.viewModel.BannerViewModel
import com.example.eccomerce_app.viewModel.CartViewModel
import com.example.eccomerce_app.viewModel.CategoryViewModel
import com.example.eccomerce_app.viewModel.CurrencyViewModel
import com.example.eccomerce_app.viewModel.GeneralSettingViewModel
import com.example.eccomerce_app.viewModel.HomeViewModel
import com.example.eccomerce_app.viewModel.OrderViewModel
import com.example.eccomerce_app.viewModel.PaymentTypeViewModel
import com.example.eccomerce_app.viewModel.StoreViewModel
import com.example.eccomerce_app.viewModel.SubCategoryViewModel
import com.example.eccomerce_app.viewModel.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.collections.isNullOrEmpty
import kotlin.toString

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class,
    ExperimentalMaterial3WindowSizeClassApi::class
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
    storeViewModel: StoreViewModel,
    subCategoryViewModel: SubCategoryViewModel,
    cartViewModel: CartViewModel,

    ) {
    val configuration = LocalConfiguration.current

    val layoutDirection = LocalLayoutDirection.current

    val context = LocalContext.current
    val activity = context as Activity


    val lazyState = rememberLazyListState()
    val state = rememberPullToRefreshState()
    val coroutine = rememberCoroutineScope()


    val windowSize = calculateWindowSizeClass(activity)

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

    // val sizeAnimation = animateDpAsState(if (!isClickingSearch.value) 80.dp else 0.dp)


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



        when (windowSize.widthSizeClass) {
            WindowWidthSizeClass.Compact -> CompactHomePageLayout(
                nav = nav,
                isRefresh = isRefresh.value,
                state = state,
                lazyState = lazyState,
                myInfo = myInfo.value,
                categories = categories.value,
                products = products.value,
                banner = banner.value,
                isClickingSearch = isClickingSearch.value,
                isLoadingMore = isLoadingMore.value,
                configuration = configuration,
                productViewModel = productViewModel,
                layoutDirection = layoutDirection,
                updateClickState = { value -> isClickingSearch.value = value },
                initialDataLoad = { showRefreshIndicator ->
                    initialDataLoad(showRefreshIndicator)
                },
                contentPadding = contentPadding
            )

            else ->
                MediumToExpandedHomePageLayout(
                    nav = nav,
                    isRefresh = isRefresh.value,
                    state = state,
                    lazyState = lazyState,
                    myInfo = myInfo.value,
                    categories = categories.value,
                    products = products.value,
                    banner = banner.value,
                    isClickingSearch = isClickingSearch.value,
                    isLoadingMore = isLoadingMore.value,
                    configuration = configuration,
                    productViewModel = productViewModel,
                    layoutDirection = layoutDirection,
                    updateClickState = { value -> isClickingSearch.value = value },
                    initialDataLoad = { showRefreshIndicator ->
                        initialDataLoad(showRefreshIndicator)
                    },
                    contentPadding = contentPadding,
                    cartViewModel = cartViewModel,
                    userViewModel = userViewModel,
                    bannerViewModel = bannerViewModel,
                    storeViewModel = storeViewModel,
                    variantViewModel = variantViewModel,
                    categoryViewModel = categoryViewModel,
                    currencyViewModel = currencyViewModel,
                    subCategoryViewModel = subCategoryViewModel

                )
        }
    }
}


@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun CompactHomePageLayout(
    nav: NavHostController,
    isRefresh: Boolean,
    state: PullToRefreshState = rememberPullToRefreshState(),
    lazyState: LazyListState = rememberLazyListState(),
    myInfo: UserModel? = null,
    categories: List<Category>? = null,
    products: List<ProductModel>? = null,
    banner: List<BannerModel>? = null,
    isClickingSearch: Boolean,
    isLoadingMore: Boolean,
    configuration: Configuration,
    productViewModel: ProductViewModel,
    layoutDirection: LayoutDirection,
    initialDataLoad: (state: Boolean) -> Unit,
    updateClickState: (state: Boolean) -> Unit,
    contentPadding: PaddingValues,
) {

    val sizeAnimation = animateDpAsState(if (!isClickingSearch) 80.dp else 0.dp)



    PullToRefreshBox(
        isRefreshing = isRefresh,
        onRefresh = {
            initialDataLoad(true)
        },
        state = state,
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = isRefresh,
                containerColor = Color.White,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                state = state
            )
        },
    )
    {
        LazyColumn(
            state = lazyState,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 15.dp + contentPadding.calculateLeftPadding(layoutDirection),
                    end = 15.dp + contentPadding.calculateRightPadding(layoutDirection),
                    top = 5.dp + contentPadding.calculateTopPadding(),
                    bottom = 5.dp + contentPadding.calculateBottomPadding()
                )
                .background(Color.White)

        ) {

            //address info
            item {
                HomeAddressComponent(
                    isPassCondition = myInfo?.address == null && categories == null,
                    screenWidth = configuration.screenWidthDp,
                    animatedComponentSize = sizeAnimation.value,
                    onPressDo = {
                        nav.navigate(Screens.EditeOrAddNewAddress)
                    },
                    address = myInfo?.address?.firstOrNull { it.isCurrent }
                )

            }

            //this the search box
            if (!products.isNullOrEmpty())
                item {
                    HomeSearchComponent(
                        isClickingSearch = isClickingSearch,
                    ) { state ->
                        updateClickState.invoke(state)
                    }
                }


            item {
                OpacityAndHideComponent(
                    isHideComponent = isClickingSearch,
                    content = {
                        when (categories == null) {
                            true -> {
                                CategoryLoadingShape()
                            }

                            else -> {
                                when (categories.isEmpty()) {
                                    true -> {}
                                    else -> {
                                        CategoryShape(
                                            categories = categories.take(4),
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
            item {
                OpacityAndHideComponent(
                    isHideComponent = isClickingSearch,
                    content = {
                        when (banner == null) {
                            true -> {
                                BannerLoading()
                            }

                            else -> {
                                if (banner.isNotEmpty())
                                    BannerPage(
                                        banners = banner,
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

            item {
                OpacityAndHideComponent(
                    isHideComponent = isClickingSearch,
                    content = {
                        Sizer(10)
                        when (products == null) {
                            true -> {
                                ProductLoading()
                            }

                            else -> {
                                if (products.isNotEmpty()) {
                                    ProductShape(
                                        products,
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
                                }
                            }
                        }
                    })
            }

            if (isLoadingMore) {

                item {
                    OpacityAndHideComponent(
                        isHideComponent = isClickingSearch,
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
                Sizer(140)
            }
        }

    }
}


enum class EnSupportScreenType { ADDRESS, CATEGORY, PRODUCTCATEGORY, PRODUCT, BANNER }

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun MediumToExpandedHomePageLayout(
    nav: NavHostController,
    isRefresh: Boolean,
    state: PullToRefreshState = rememberPullToRefreshState(),
    lazyState: LazyListState = rememberLazyListState(),
    myInfo: UserModel? = null,
    categories: List<Category>? = null,
    products: List<ProductModel>? = null,
    banner: List<BannerModel>? = null,
    isClickingSearch: Boolean,
    isLoadingMore: Boolean,
    configuration: Configuration,
    layoutDirection: LayoutDirection,
    initialDataLoad: (state: Boolean) -> Unit,
    updateClickState: (state: Boolean) -> Unit,
    productViewModel: ProductViewModel,
    categoryViewModel: CategoryViewModel,
    storeViewModel: StoreViewModel,
    subCategoryViewModel: SubCategoryViewModel,
    cartViewModel: CartViewModel,
    variantViewModel: VariantViewModel,
    bannerViewModel: BannerViewModel,
    currencyViewModel: CurrencyViewModel,
    userViewModel: UserViewModel,
    contentPadding: PaddingValues
) {

    val sizeAnimation = animateDpAsState(if (!isClickingSearch) 80.dp else 0.dp)

    val supportScreenType = retain { mutableStateOf<EnSupportScreenType?>(null) }

    val selectedCategoryId = retain { mutableStateOf<UUID?>(null) }
    val selectedStoreId = retain { mutableStateOf<UUID?>(null) }
    val selectedProductId = retain { mutableStateOf<UUID?>(null) }




    Row(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(
                start = 15.dp + contentPadding.calculateLeftPadding(layoutDirection),
                end = 15.dp + contentPadding.calculateRightPadding(layoutDirection),
                top = 5.dp + contentPadding.calculateTopPadding(),
                bottom = 5.dp + contentPadding.calculateBottomPadding()
            )

    ) {
        PullToRefreshBox(
            isRefreshing = isRefresh,
            onRefresh = {
                initialDataLoad(true)
            },
            state = state,
            indicator = {
                Indicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    isRefreshing = isRefresh,
                    containerColor = Color.White,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    state = state
                )
            },
            modifier = Modifier.weight(1f)
        )
        {
            LazyColumn(
                state = lazyState,
                modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .fillMaxSize()

            ) {

                //address info
                item(key = myInfo?.address) {
                    HomeAddressComponent(
                        isPassCondition = myInfo?.address == null && categories == null,
                        screenWidth = configuration.screenWidthDp,
                        animatedComponentSize = sizeAnimation.value,
                        onPressDo = {
                            supportScreenType.value = EnSupportScreenType.ADDRESS;
                        },
                        address = myInfo?.address?.firstOrNull { it.isCurrent }
                    )

                }

                //this the search box
                if (!products.isNullOrEmpty())
                    item {
                        HomeSearchComponent(
                            isClickingSearch = isClickingSearch,
                        ) { state ->
                            updateClickState.invoke(state)
                        }
                    }


                item(key = categories) {
                    OpacityAndHideComponent(
                        isHideComponent = isClickingSearch,
                        content = {
                            when (categories == null) {
                                true -> {
                                    CategoryLoadingShape()
                                }

                                else -> {
                                    when (categories.isEmpty()) {
                                        true -> {}
                                        else -> {
                                            CategoryShape(
                                                categories = categories.take(4),
                                                productViewModel = productViewModel,
                                                onPressDo = { id ->
                                                    selectedCategoryId.value = id
                                                    productViewModel.getProductsByCategoryID(
                                                        pageNumber = 1,
                                                        categoryId = id
                                                    )
                                                    supportScreenType.value =
                                                        EnSupportScreenType.PRODUCTCATEGORY

                                                },
                                                onPressViewAlDo = {
                                                    supportScreenType.value =
                                                        EnSupportScreenType.CATEGORY
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        })
                }

                //banner section
                item(key = banner) {
                    OpacityAndHideComponent(
                        isHideComponent = isClickingSearch,
                        content = {
                            when (banner == null) {
                                true -> {
                                    BannerLoading()
                                }

                                else -> {
                                    if (banner.isNotEmpty())
                                        BannerPage(
                                            banners = banner,
                                            isMe = false,
                                            onPressDo = { id ->
                                                selectedStoreId.value = id
                                                supportScreenType.value =
                                                    EnSupportScreenType.BANNER
                                            }
                                        )
                                }
                            }
                        })


                }


                //product

                item(key = products) {
                    OpacityAndHideComponent(
                        isHideComponent = isClickingSearch,
                        content = {
                            Sizer(10)
                            when (products == null) {
                                true -> {
                                    ProductLoading()
                                }

                                else -> {
                                    if (products.isNotEmpty()) {
                                        ProductShape(
                                            products,
                                            onPressDo = { value, isFromHome, isCanNavToStore ->
                                                selectedProductId.value = value
                                                supportScreenType.value =
                                                    EnSupportScreenType.PRODUCT
                                            })
                                    }
                                }
                            }
                        })
                }

                if (isLoadingMore) {

                    item {
                        OpacityAndHideComponent(
                            isHideComponent = isClickingSearch,
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
                    Sizer(140)
                }
            }

        }

        AnimatedVisibility(
            visible = supportScreenType.value != null,
            modifier = Modifier
                .background(Color.White)
                .weight(1f)
        ) {
            when (supportScreenType.value) {

                EnSupportScreenType.CATEGORY -> {
                    CategoryScreen(
                        nav = nav,
                        categoryViewModel = categoryViewModel,
                        productViewModel = productViewModel,
                        isShowArrowBackNavIcon = false
                    )
                }

                EnSupportScreenType.PRODUCTCATEGORY -> {
                    ProductCategoryScreen(
                        nav = nav,
                        categoryId = if (selectedCategoryId.value != null) selectedCategoryId.value.toString() else "",
                        categoryViewModel = categoryViewModel,
                        productViewModel = productViewModel ,
                        isShowArrowBackIcon = false
                    )

                }

                EnSupportScreenType.PRODUCT -> {
                    ProductDetail(
                        nav = nav,
                        cartViewModel = cartViewModel,
                        productID = selectedProductId.value.toString(),
                        isFromHome = true,
                        variantViewModel = variantViewModel,
                        storeViewModel = storeViewModel,
                        bannerViewModel = bannerViewModel,
                        subCategoryViewModel = subCategoryViewModel,
                        productViewModel = productViewModel,
                        userViewModel = userViewModel,
                        currencyViewModel = currencyViewModel,
                        isCanNavigateToStore = true,
                        isShowArrowBackIcon = false
                        )

                }

                EnSupportScreenType.BANNER -> {
                    StoreScreen(
                        nav = nav,
                        categoryViewModel = categoryViewModel,
                        userViewModel = userViewModel,
                        productViewModel = productViewModel,
                        storeViewModel = storeViewModel,
                        isFromHome = true,
                        copyStoreId = selectedStoreId.toString(),
                        bannerViewModel = bannerViewModel,
                        subCategoryViewModel = subCategoryViewModel,
                        isShowArrowBackIcon = false
                    )
                }

                EnSupportScreenType.ADDRESS -> {
                    EditOrAddLocationScreen(
                        nav = nav,
                        userViewModel = userViewModel,
                        storeViewModel = storeViewModel,
                        isShowArrowBackIcon = false
                    )
                }

                else -> {}
            }

        }
    }


}








