package com.example.eccomerce_app.ui.view.account

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.e_commercompose.R
import com.example.eccomerce_app.util.General
import com.example.eccomerce_app.ui.Screens
import com.example.eccomerce_app.ui.component.AccountCustomBottom
import com.example.eccomerce_app.ui.component.LogoutButton
import com.example.e_commercompose.ui.theme.CustomColor
import com.example.eccomerce_app.ui.component.SharedAppBar
import com.example.eccomerce_app.ui.view.account.store.CreateProductScreen
import com.example.eccomerce_app.ui.view.account.store.StoreScreen
import com.example.eccomerce_app.ui.view.account.store.delivery.DeliveriesListScreen
import com.example.eccomerce_app.ui.view.address.AddressHomeScreen
import com.example.eccomerce_app.ui.view.address.EditOrAddLocationScreen
import com.example.eccomerce_app.ui.view.address.MapHomeScreen
import com.example.eccomerce_app.util.General.currentLocal
import com.example.eccomerce_app.util.General.whenLanguageUpdateDo
import com.example.eccomerce_app.viewModel.AuthViewModel
import com.example.eccomerce_app.viewModel.BannerViewModel
import com.example.eccomerce_app.viewModel.CartViewModel
import com.example.eccomerce_app.viewModel.CategoryViewModel
import com.example.eccomerce_app.viewModel.CurrencyViewModel
import com.example.eccomerce_app.viewModel.DeliveryViewModel
import com.example.eccomerce_app.viewModel.MapViewModel
import com.example.eccomerce_app.viewModel.OrderItemsViewModel
import com.example.eccomerce_app.viewModel.ProductViewModel
import com.example.eccomerce_app.viewModel.StoreViewModel
import com.example.eccomerce_app.viewModel.SubCategoryViewModel
import com.example.eccomerce_app.viewModel.UserViewModel
import com.example.eccomerce_app.viewModel.VariantViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AccountPage(
    nav: NavHostController,
    userViewModel: UserViewModel,
    orderItemsViewModel: OrderItemsViewModel,
    authViewModel: AuthViewModel,
    productViewModel: ProductViewModel,
    currencyViewModel: CurrencyViewModel,
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val coroutine = rememberCoroutineScope()

    val myInfo = userViewModel.userInfo.collectAsStateWithLifecycle()
    val currentLocale = currentLocal.collectAsStateWithLifecycle()
    val currencies = currencyViewModel.currenciesList.collectAsStateWithLifecycle()
    val storeId = myInfo.value?.storeId

    val isChangingCurrency = remember { mutableStateOf(false) }
    val isShowCurrencies = remember { mutableStateOf(false) }
    val isChangingLanguage = remember { mutableStateOf(false) }
    val isExpandLanguage = remember { mutableStateOf(false) }

    fun updateConditionValue(
        isChangingCurrencyValue: Boolean? = null,
        isShowCurrenciesValue: Boolean? = null,
        isChangingLanguageValue: Boolean? = null,
        isExpandLanguageValue: Boolean? = null
    ) {
        if (isChangingCurrencyValue != null) isChangingCurrency.value = isChangingCurrencyValue
        if (isShowCurrenciesValue != null) isShowCurrencies.value = isShowCurrenciesValue
        if (isChangingLanguageValue != null) isChangingLanguage.value = isChangingLanguageValue
        if (isExpandLanguageValue != null) isExpandLanguage.value = isExpandLanguageValue
    }

    val updateDirection = remember {
        derivedStateOf {
            if (currentLocale.value == "ar") LayoutDirection.Rtl else LayoutDirection.Ltr
        }
    }

    fun updateLanguage(lang: String) {
        coroutine.launch {
            updateConditionValue(isChangingLanguageValue = true)
            delay(100)
            val currentLange = if (lang == "العربية") {
                if (currentLocale.value == "en") "ar" else ""
            } else {
                if (currentLocale.value == "ar") "en" else ""
            }
            if (currentLange.isEmpty()) {
                updateConditionValue(isChangingLanguageValue = false)
                return@launch
            }
            async { userViewModel.updateCurrentLocale(currentLange) }.await()
            currentLocal.emit(currentLange)
            whenLanguageUpdateDo(currentLange, context)
            updateConditionValue(isChangingLanguageValue = false, isExpandLanguageValue = false)
        }
    }

    fun updateProductCurrency(symbol: String) {
        updateConditionValue(isChangingLanguageValue = true)
        productViewModel.setDefaultCurrency(symbol) { value ->
            updateConditionValue(isChangingLanguageValue = value)
        }
    }

    fun logout() {
        authViewModel.logout()
        nav.navigate(Screens.AuthGraph) {
            popUpTo(nav.graph.id) { inclusive = true }
        }
    }

    LaunchedEffect(isChangingLanguage.value) {
        if (!isChangingLanguage.value) updateConditionValue(isShowCurrenciesValue = false)
    }

    CompositionLocalProvider(LocalLayoutDirection provides updateDirection.value) {

        Scaffold(
            topBar = {
                SharedAppBar(
                    title = stringResource(R.string.account),
                    nav = nav,
                    scrollBehavior = scrollBehavior
                )
            }
        )
        { padding ->
            if (isChangingLanguage.value || isChangingCurrency.value) {
                Dialog(onDismissRequest = {}) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .background(Color.White, RoundedCornerShape(15.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = CustomColor.primaryColor700,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(padding)
                    .padding(horizontal = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AccountCustomBottom(
                    stringResource(R.string.your_profile),
                    R.drawable.user, {
                            nav.navigate(Screens.Profile)
                    })
                HorizontalDivider(
                    Modifier
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(CustomColor.neutralColor200)
                )

                AccountCustomBottom(
                    stringResource(R.string.address),
                    R.drawable.location_address_list, {
                            nav.navigate(Screens.EditeOrAddNewAddress)
                    })
                HorizontalDivider(
                    Modifier
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(CustomColor.neutralColor200)
                )

                AccountCustomBottom(
                    stringResource(R.string.my_store),
                    R.drawable.store, {
                            nav.navigate(Screens.Store(storeId.toString(), false))
                    })
                HorizontalDivider(
                    Modifier
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(CustomColor.neutralColor200)
                )

                if (myInfo.value?.storeId != null) {
                    AccountCustomBottom(
                        stringResource(R.string.order_for_my_store),
                        R.drawable.order_belong_to_store, {
                                orderItemsViewModel.getMyOrderItemBelongToMyStore(1, false)
                                nav.navigate(Screens.OrderForMyStore)
                        })
                    HorizontalDivider(
                        Modifier
                            .height(1.dp)
                            .fillMaxWidth()
                            .background(CustomColor.neutralColor200)
                    )
                }

                AccountCustomBottom(
                    stringResource(R.string.exchange_currency),
                    R.drawable.currency_exchange, {
                        updateConditionValue(isShowCurrenciesValue = true)
                    },
                    additionalComponent = {
                        Box {
                            DropdownMenu(
                                containerColor = Color.White,
                                expanded = isShowCurrencies.value,
                                onDismissRequest = {
                                    updateConditionValue(
                                        isShowCurrenciesValue = false
                                    )
                                }
                            ) {
                                currencies.value?.fastForEach { lang ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                lang.name,
                                                fontFamily = General.satoshiFamily,
                                                fontSize = 18.sp
                                            )
                                        },
                                        onClick = { updateProductCurrency(lang.symbol) }
                                    )
                                }
                            }
                        }
                    })
                HorizontalDivider(
                    Modifier
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(CustomColor.neutralColor200)
                )

                AccountCustomBottom(
                    "Language",
                    R.drawable.language, {
                        if (currentLocale.value == "en") updateLanguage("العربية") else updateLanguage(
                            "English"
                        )
                    },
                    additionalComponent = {
                        Box {
                            TextButton(onClick = {
                                updateConditionValue(
                                    isExpandLanguageValue = true
                                )
                            }) {
                                Text(
                                    if (currentLocale.value == "en") "English" else "العربية",
                                    color = CustomColor.neutralColor950
                                )
                            }
                            DropdownMenu(
                                containerColor = Color.White,
                                expanded = isExpandLanguage.value,
                                onDismissRequest = {
                                    updateConditionValue(
                                        isExpandLanguageValue = false
                                    )
                                }
                            ) {
                                listOf("العربية", "English").forEach { lang ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                lang,
                                                fontFamily = General.satoshiFamily,
                                                fontSize = 18.sp
                                            )
                                        },
                                        onClick = { updateLanguage(lang) }
                                    )
                                }
                            }
                        }
                    }
                )
                HorizontalDivider(
                    Modifier
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(CustomColor.neutralColor200)
                )

                LogoutButton(
                    stringResource(R.string.logout),
                    R.drawable.logout
                ) { logout() }
            }
        }
    }
}
