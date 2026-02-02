package com.example.eccomerce_app.ui.view.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.example.eccomerce_app.util.General
import com.example.e_commercompose.ui.component.Sizer
import com.example.e_commercompose.ui.theme.CustomColor
import com.example.eccomerce_app.viewModel.CartViewModel
import com.example.e_commercompose.R
import com.example.eccomerce_app.ui.Screens
import com.example.e_commercompose.ui.component.CustomButton
import com.example.e_commercompose.ui.component.LabelValueRow
import com.example.e_commercompose.ui.component.SwapToDismiss
import com.example.eccomerce_app.ui.component.SharedAppBar
import com.example.eccomerce_app.ui.view.address.EditOrAddLocationScreen
import com.example.eccomerce_app.ui.view.checkout.CheckoutScreen
import com.example.eccomerce_app.viewModel.CurrencyViewModel
import com.example.eccomerce_app.viewModel.GeneralSettingViewModel
import com.example.eccomerce_app.viewModel.OrderViewModel
import com.example.eccomerce_app.viewModel.PaymentTypeViewModel
import com.example.eccomerce_app.viewModel.PaymentViewModel
import com.example.eccomerce_app.viewModel.StoreViewModel
import com.example.eccomerce_app.viewModel.VariantViewModel
import com.example.eccomerce_app.viewModel.UserViewModel
import kotlinx.coroutines.launch

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun CartScreen(
    nav: NavHostController,
    cartViewModel: CartViewModel,
    storeViewModel: StoreViewModel,
    userViewModel: UserViewModel,
    variantViewModel: VariantViewModel,
    generalSettingViewModel: GeneralSettingViewModel,
    orderViewModel: OrderViewModel,
    paymentTypeViewModel: PaymentTypeViewModel,
    paymentViewModel: PaymentViewModel,
    currencyViewModel: CurrencyViewModel
) {
    val context = LocalContext.current
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp

    val coroutine = rememberCoroutineScope()

    val cardData = cartViewModel.cartItems.collectAsStateWithLifecycle()
    val variants = variantViewModel.variants.collectAsStateWithLifecycle()
    val stores = storeViewModel.stores.collectAsStateWithLifecycle()
    val userInfo = userViewModel.userInfo.collectAsStateWithLifecycle()

    val currentAddress = userInfo.value?.address?.firstOrNull { it -> it.isCurrent }

    val snackBarHostState = remember { SnackbarHostState() }

    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()

    NavigableListDetailPaneScaffold(
        navigator = navigator,
        listPane = {
            Scaffold(
                snackbarHost = {
                    SnackbarHost(
                        hostState = snackBarHostState,
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                },
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                topBar = {
                    SharedAppBar(title = stringResource(R.string.my_cart))

                },
                floatingActionButton = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 65.dp)
                            .offset(x = 16.dp),
                    ) {
                        if (cardData.value.totalPrice != 0L) {
                            Row(
                                modifier = Modifier
                                    .background(Color.White)
                                    .padding(top = 15.dp)
                                    .padding(horizontal = 15.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                LabelValueRow(stringResource(R.string.total),"$${cardData.value.totalPrice}")

                            }

                            Sizer(30)
                            Box(
                                modifier = Modifier
                                    .background(Color.White)
                                    .padding(horizontal = 15.dp)
                            ) {
                                CustomButton(
                                    buttonTitle = stringResource(R.string.go_to_checkout), operation = {
                                        coroutine.launch {
                                            cartViewModel.calculateOrderDistanceToUser(
                                                stores = stores.value,
                                                currentAddress = currentAddress
                                            )
                                            navigator.navigateTo(
                                                ListDetailPaneScaffoldRole.Detail,
                                                Screens.Checkout)
                                        }
                                    })
                            }
                        }

                    }
                })
            { scaffoldState ->
                scaffoldState.calculateTopPadding()
                scaffoldState.calculateBottomPadding()
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(scaffoldState)
                        .background(Color.White), verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(cardData.value.cartProducts.size) { index ->
                        SwapToDismiss(component = {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .wrapContentHeight()
                                            .width((screenWidth - 120).dp),

                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        SubcomposeAsyncImage(
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .height(80.dp)
                                                .width(80.dp)
                                                .clip(RoundedCornerShape(8.dp)),
                                            model = General.handlingImageForCoil(
                                                cardData.value.cartProducts[index].thumbnail, context
                                            ),
                                            contentDescription = "",
                                            loading = {
                                                Box(
                                                    modifier = Modifier.fillMaxSize(),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    CircularProgressIndicator(
                                                        color = Color.Black,
                                                        modifier = Modifier.size(53.dp)
                                                    )
                                                }
                                            },
                                        )
                                        Sizer(width = 10)
                                        Column {
                                            Text(
                                                cardData.value.cartProducts[index].name,
                                                fontFamily = General.satoshiFamily,
                                                fontWeight = FontWeight.Medium,
                                                fontSize = (16).sp,
                                                color = CustomColor.neutralColor950,
                                                textAlign = TextAlign.Center,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Sizer(width = 5)
                                            cardData.value.cartProducts[index].productVariants.forEach { value ->

                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    val title =
                                                        variants.value?.firstOrNull { it.id == value.variantId }?.name
                                                            ?: ""
                                                    Text(
                                                        (variants.value?.firstOrNull { it.id == value.variantId }?.name
                                                            ?: "") + ": ",
                                                        fontFamily = General.satoshiFamily,
                                                        fontWeight = FontWeight.Normal,
                                                        fontSize = (16).sp,
                                                        color = CustomColor.neutralColor950,
                                                        textAlign = TextAlign.Center
                                                    )
                                                    when (title == "Color") {
                                                        true -> {
                                                            val colorValue =
                                                                General.convertColorToInt(value.name)

                                                            if (colorValue != null) Box(
                                                                modifier = Modifier
                                                                    .height(20.dp)
                                                                    .width(20.dp)
                                                                    .background(
                                                                        colorValue,
                                                                        RoundedCornerShape(20.dp)
                                                                    )

                                                                    .clip(RoundedCornerShape(20.dp))
                                                            )
                                                        }

                                                        else -> {
                                                            Box(
                                                                modifier = Modifier

                                                                    .clip(RoundedCornerShape(20.dp)),
                                                                contentAlignment = Alignment.Center
                                                            ) {
                                                                Text(
                                                                    text = value.name,
                                                                    fontFamily = General.satoshiFamily,
                                                                    fontWeight = FontWeight.Normal,
                                                                    fontSize = (16).sp,
                                                                    color = CustomColor.neutralColor800,
                                                                    textAlign = TextAlign.Center
                                                                )
                                                            }
                                                        }
                                                    }

                                                }
                                            }
                                        }

                                    }


                                    Row(
                                        modifier = Modifier.width(90.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(25.dp)
                                                .background(
                                                    CustomColor.neutralColor200, RoundedCornerShape(8.dp)
                                                )
                                                .clip(RoundedCornerShape(8.dp))
                                                .clickable {
                                                    cartViewModel.decreaseCardItem(cardData.value.cartProducts[index].id)
                                                }, contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                ImageVector.vectorResource(R.drawable.baseline_horizontal),
                                                "",
                                                tint = Color.Black,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                        Text(
                                            "${cardData.value.cartProducts[index].quantity}",
                                            fontFamily = General.satoshiFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = (24).sp,
                                            color = CustomColor.neutralColor950,
                                            textAlign = TextAlign.Center
                                        )
                                        Box(
                                            modifier = Modifier
                                                .size(25.dp)
                                                .background(
                                                    CustomColor.primaryColor700, RoundedCornerShape(8.dp)
                                                )
                                                .clip(RoundedCornerShape(8.dp))
                                                .clickable {
                                                    cartViewModel.increaseCardItem(cardData.value.cartProducts[index].id)
                                                }, contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                Icons.Default.Add,
                                                "",
                                                tint = Color.White,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .padding(top = 5.dp)
                                        .height(1.dp)
                                        .fillMaxWidth()
                                        .background(CustomColor.neutralColor200)
                                )
                            }

                        }, index = cardData.value.cartProducts[index].id, delete = { index ->
                            cartViewModel.removeItemFromCard(index)
                        }

                        )
                    }

                    item {
                        Box(modifier = Modifier.height(190.dp))
                    }
                }
            }

        },
        detailPane = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                when(val content = navigator.currentDestination?.contentKey)
                {
                    is Screens.Checkout -> CheckoutScreen(
                        nav = nav,
                        navBack = navigator,
                        cartViewModel =cartViewModel,
                        generalSettingViewModel =generalSettingViewModel,
                        userViewModel = userViewModel,
                        orderViewModel =orderViewModel,
                        currencyViewModel =currencyViewModel,
                        paymentTypeViewModel =paymentTypeViewModel,
                        paymentViewModel =paymentViewModel
                    )

                    is Screens.EditeOrAddNewAddress -> EditOrAddLocationScreen(
                        nav = navigator,
                        userViewModel=userViewModel,
                        storeViewModel = storeViewModel,
                        cartViewModel = cartViewModel,

                    )
                }
            }
        }
    )

}


