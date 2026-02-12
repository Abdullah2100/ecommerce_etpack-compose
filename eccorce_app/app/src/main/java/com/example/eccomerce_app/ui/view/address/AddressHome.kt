package com.example.eccomerce_app.ui.view.address

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import com.example.e_commercompose.ui.theme.CustomColor
import com.example.e_commercompose.R
import com.example.eccomerce_app.util.General
import com.example.e_commercompose.model.enMapType
import com.example.eccomerce_app.ui.Screens
import com.example.e_commercompose.ui.component.CustomButton
import com.example.e_commercompose.ui.component.CustomTitleButton
import com.example.eccomerce_app.ui.component.Sizer
import com.example.eccomerce_app.viewModel.BannerViewModel
import com.example.eccomerce_app.viewModel.CartViewModel
import com.example.eccomerce_app.viewModel.CategoryViewModel
import com.example.eccomerce_app.viewModel.GeneralSettingViewModel
import com.example.eccomerce_app.viewModel.OrderViewModel
import com.example.eccomerce_app.viewModel.ProductViewModel
import com.example.eccomerce_app.viewModel.StoreViewModel
import com.example.eccomerce_app.viewModel.UserViewModel
import com.example.eccomerce_app.viewModel.VariantViewModel
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("LocalContextGetResourceValueCall")
@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class,
    ExperimentalMaterial3WindowSizeClassApi::class
)
@Composable
fun AddressHomeScreen(
    nav: NavHostController? = null,
    userViewModel: UserViewModel,


    bannerViewModel: BannerViewModel,
    categoryViewModel: CategoryViewModel,
    variantViewModel: VariantViewModel,
    productViewModel: ProductViewModel,
    generalSettingViewModel: GeneralSettingViewModel,
    orderViewModel: OrderViewModel,
    cartViewModel: CartViewModel,
    storeViewModel: StoreViewModel


) {
    val context = LocalContext.current
    val layoutDirection = LocalLayoutDirection.current

    val activity = context as Activity
    val windowSize = calculateWindowSizeClass(activity)

    val fontScall = LocalDensity.current.fontScale

    val coroutine = rememberCoroutineScope()


    val currentLocation = retain { mutableStateOf<Location?>(null) }
    val isExpandedScree = windowSize.widthSizeClass == WindowWidthSizeClass.Expanded ||windowSize.widthSizeClass == WindowWidthSizeClass.Medium


    val snackBarHostState = remember { SnackbarHostState() }

    val locationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val requestPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val arePermissionsGranted = permissions.values.reduce { acc, next -> acc && next }

            if (arePermissionsGranted) {
                if (ActivityCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.you_must_enable_location_permission_to_app),
                        Toast.LENGTH_SHORT
                    ).show()

                    return@rememberLauncherForActivityResult
                }

                locationClient.lastLocation
                    .apply {
                        addOnSuccessListener { location ->
                            if (!isExpandedScree) {
                                if (location != null)

                                    nav!!.navigate(
                                        Screens.MapScreen(
                                            lognit = location.longitude,
                                            latitt = location.latitude,
                                            isFromLogin = true,
                                            mapType = enMapType.My
                                        )
                                    )
                                else
                                    coroutine.launch {
                                        snackBarHostState.showSnackbar(context.getString(R.string.you_should_enable_location_services))
                                    }
                            } else currentLocation.value = location

                        }
                    }
                    .addOnFailureListener { fail ->
                        Log.d(
                            "contextError",
                            "the current location is null ${fail.stackTrace}"
                        )

                    }


            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.location_permission_denied), Toast.LENGTH_SHORT
                ).show()
            }
        }
    )

    fun requestLocationFun() {
        requestPermission.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }


    LaunchedEffect(Unit) {
        userViewModel.getMyInfo()
    }

    LaunchedEffect(Unit) {
        if (isExpandedScree)
            requestLocationFun()
    }




    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    )
    { paddingValues ->
        paddingValues.calculateTopPadding()
        paddingValues.calculateBottomPadding()

         when (windowSize.widthSizeClass) {
               WindowWidthSizeClass.Compact->
                CompactToMediumAddressHomeLayout(
                    contentPadding = paddingValues,
                    fontScall = fontScall,
                    navToMapScreen = {
//                        requestLocationFun()
                        requestPermission.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                                     },
                    navToUserAddressList = {
                        userViewModel.getMyInfo()
                        nav?.navigate(Screens.PickCurrentAddress)
                    },
                    layoutDirection = layoutDirection

                )

             WindowWidthSizeClass.Medium,    WindowWidthSizeClass.Expanded ->
                ExpandedAddressHomeLayout(
                    nav = nav!!,
                    contentPadding = paddingValues,
                    fontScall = fontScall,
                    userLocation = currentLocation.value,
                    bannerViewModel = bannerViewModel,
                    categoryViewModel = categoryViewModel,
                    variantViewModel = variantViewModel,
                    productViewModel = productViewModel,
                    orderViewModel = orderViewModel,
                    generalSettingViewModel = generalSettingViewModel,
                    cartViewModel = cartViewModel,
                    storeViewModel = storeViewModel,
                    requestLocationPermission = {
                        requestLocationFun()
                    },
                    userViewModel = userViewModel,
                    coroutine = coroutine,
                    layoutDirection = layoutDirection
                )


        }
    }

}

@Composable
fun CompactToMediumAddressHomeLayout(
    contentPadding: PaddingValues,
    fontScall: Float,
    navToMapScreen: () -> Unit,
    navToUserAddressList: () -> Unit,
    layoutDirection: LayoutDirection
) {
    LazyColumn(
        modifier = Modifier
            .background(Color.White)
            .padding(
                start = 15.dp + contentPadding.calculateLeftPadding(layoutDirection),
                end = 15.dp + contentPadding.calculateRightPadding(layoutDirection),
                top = 5.dp+contentPadding.calculateTopPadding(),
                bottom = 5.dp+contentPadding.calculateBottomPadding()
            )
            .padding(horizontal = 10.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        item {

            Box(
                modifier = Modifier
                    .height(80.dp)
                    .width(80.dp)
                    .background(
                        CustomColor.primaryColor50,
                        RoundedCornerShape(40.dp),
                    ),
                contentAlignment = Alignment.Center
            )
            {
                Icon(
                    imageVector = ImageVector
                        .vectorResource(R.drawable.location),
                    contentDescription = "",
                    tint = CustomColor.primaryColor700
                )
            }

            Sizer(50)
            Text(
                stringResource(R.string.what_is_your_location),
                fontFamily = General.satoshiFamily,
                fontWeight = FontWeight.Bold,
                fontSize = (24 / fontScall).sp,
                color = CustomColor.neutralColor950,
                textAlign = TextAlign.Center

            )
            Sizer(8)
            Text(
                stringResource(R.string.we_need_to_know_your_location_in_order_to_suggest_nearby_services),
                fontFamily = General.satoshiFamily,
                fontWeight = FontWeight.Normal,
                fontSize = (16 / fontScall).sp,
                color = CustomColor.neutralColor800,
                textAlign = TextAlign.Center
            )
            Sizer(50)
            CustomButton(
                operation = {
                    navToMapScreen()
                },
                buttonTitle = stringResource(R.string.allow_location_access),
                color = CustomColor.primaryColor700
            )
            Sizer(20)
            CustomTitleButton(
                operation = {
                    navToUserAddressList()
                },
                buttonTitle = stringResource(R.string.enter_location_manually),
                color = CustomColor.primaryColor700
            )


        }

    }

}


enum class enSideType { MapScreen, ListAddress }

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun ExpandedAddressHomeLayout(
    nav: NavHostController,
    contentPadding: PaddingValues,
    fontScall: Float,
    userLocation: Location? = null,
    userViewModel: UserViewModel,
    bannerViewModel: BannerViewModel,
    categoryViewModel: CategoryViewModel,
    variantViewModel: VariantViewModel,
    productViewModel: ProductViewModel,
    generalSettingViewModel: GeneralSettingViewModel,
    orderViewModel: OrderViewModel,
    cartViewModel: CartViewModel,
    storeViewModel: StoreViewModel,
    requestLocationPermission: () -> Unit,
    coroutine: CoroutineScope,
    layoutDirection: LayoutDirection

) {
    val config = LocalConfiguration.current
    val scrollState = rememberScrollState()

    val screenWidth = config.screenWidthDp
    val screenHeight = config.screenHeightDp.dp

    val halfScreenWidth = (screenWidth.dp / 2) - 20.dp

    val isMapScreen = retain { mutableStateOf<enSideType?>(null) }

    val isSwitchScreen = remember() { mutableStateOf(false) }


    fun switchToMapScreen(isMapScreenBotton: Boolean = true) {
        when (userLocation == null) {
            true -> {
                requestLocationPermission()
            }

            else -> {
                //this to apply dialog effect between switch component
                isSwitchScreen.value = true

                //this to decided the type of
                when (isMapScreenBotton) {
                    true -> {
                        isMapScreen.value = enSideType.MapScreen
                    }

                    else -> {
                        isMapScreen.value = enSideType.ListAddress
                    }
                }
            }
        }
    }


    LaunchedEffect(isSwitchScreen.value) {
        if (isSwitchScreen.value)
            coroutine.launch {
                delay(20)
                isSwitchScreen.value = false

            }
    }

    Row(
        modifier = Modifier
            .background(Color.White)
            .padding(
                start = 15.dp + contentPadding.calculateLeftPadding(layoutDirection),
                end = 15.dp + contentPadding.calculateRightPadding(layoutDirection),
                top = 5.dp+contentPadding.calculateTopPadding(),
                bottom = 5.dp+contentPadding.calculateBottomPadding()
            )
            .padding(horizontal = 10.dp)
            .height(screenHeight)
            .fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    )
    {

        Column(
            modifier = Modifier
                .height(screenHeight)
                .width(halfScreenWidth - 14.dp)
                .fillMaxHeight()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {
            Box(
                modifier = Modifier
                    .height(80.dp)
                    .width(80.dp)
                    .background(
                        CustomColor.primaryColor50,
                        RoundedCornerShape(40.dp),
                    ),
                contentAlignment = Alignment.Center
            )
            {
                Icon(
                    imageVector = ImageVector
                        .vectorResource(R.drawable.location),
                    contentDescription = "",
                    tint = CustomColor.primaryColor700
                )
            }

            Sizer(50)
            Text(
                stringResource(R.string.what_is_your_location),
                fontFamily = General.satoshiFamily,
                fontWeight = FontWeight.Bold,
                fontSize = (24 / fontScall).sp,
                color = CustomColor.neutralColor950,
                textAlign = TextAlign.Center

            )
            Sizer(8)
            Text(
                stringResource(R.string.we_need_to_know_your_location_in_order_to_suggest_nearby_services),
                fontFamily = General.satoshiFamily,
                fontWeight = FontWeight.Normal,
                fontSize = (16 / fontScall).sp,
                color = CustomColor.neutralColor800,
                textAlign = TextAlign.Center
            )
            Sizer(50)
            CustomButton(
                operation = {
                    switchToMapScreen(true)
                },
                buttonTitle = stringResource(R.string.allow_location_access),
                color = CustomColor.primaryColor700
            )
            Sizer(20)
            CustomTitleButton(
                operation = {
                    switchToMapScreen(false)
                },
                buttonTitle = stringResource(R.string.enter_location_manually),
                color = CustomColor.primaryColor700
            )
        }


        Sizer(width = 20)

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .height(screenHeight)
                .width(screenWidth.dp-((screenWidth.dp/3) - 10.dp))
            ,
            contentAlignment = Alignment.Center
        ) {
            when (isSwitchScreen.value) {
                true -> {
                    CircularProgressIndicator()
                }

                false -> {
                    when (isMapScreen.value) {
                        enSideType.MapScreen -> {
                            MapHomeScreen(
                                nav = nav,
                                userViewModel = userViewModel,
                                longitude = userLocation?.longitude,
                                latitude = userLocation?.latitude,
                                cartViewModel = cartViewModel,
                                storeViewModel = storeViewModel,
                                isShowBackNavIcon = false
                            )
                        }

                        enSideType.ListAddress -> {
                            PickCurrentAddressFromAddressScreen(
                                nav = nav,
                                bannerViewModel = bannerViewModel,
                                categoryViewModel = categoryViewModel,
                                variantViewModel = variantViewModel,
                                generalSettingViewModel = generalSettingViewModel,
                                orderViewModel = orderViewModel,
                                productViewModel = productViewModel,
                                userViewModel = userViewModel,
                                isShowBackIcon = false
                            )
                        }

                        else -> {
                            Text("No Item Selected , or No Permission is Appalled")
                        }
                    }

                }
            }

        }


    }

}


