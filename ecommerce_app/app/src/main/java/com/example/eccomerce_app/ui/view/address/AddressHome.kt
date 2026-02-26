package com.example.eccomerce_app.ui.view.address

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.common.model.enMapType
import com.example.e_commercompose.ui.theme.CustomColor
import com.example.e_commercompose.R
import com.example.eccomerce_app.util.General
import com.example.eccomerce_app.ui.Screens
import com.example.e_commercompose.ui.component.CustomButton
import com.example.e_commercompose.ui.component.CustomTitleButton
import com.example.eccomerce_app.ui.component.Sizer
import com.example.eccomerce_app.viewModel.UserViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.launch

@SuppressLint("LocalContextGetResourceValueCall", "MissingPermission")
@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun AddressHomeScreen(
    nav: NavHostController? = null,
    userViewModel: UserViewModel,


) {
    val layoutDirection = LocalLayoutDirection.current

    val fontScall = LocalDensity.current.fontScale

    val coroutine = rememberCoroutineScope()

    val context = LocalContext.current

    val snackBarHostState = remember { SnackbarHostState() }

    val locationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val requestPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val arePermissionsGranted = permissions.values.all { it }

            if (arePermissionsGranted && General.isLocationServiceEnable(context) ) {
                val cts = CancellationTokenSource()
                locationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cts.token)
                    .addOnSuccessListener { location ->
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

                    }
                    .addOnCanceledListener {
                        Log.d("contextError", "this from cancel")
                    }
                    .addOnFailureListener { fail ->
                        Log.d("contextError", "this from fail ${fail.message}")

                        coroutine.launch {
                            fail.message?.let { snackBarHostState.showSnackbar(it) }
                        }
                    }

            } else {
                coroutine.launch {
                    snackBarHostState.showSnackbar(context.getString(R.string.location_permission_denied))
                }
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





    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
    )
    { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .background(Color.White)
                .padding(
                    start = 15.dp + paddingValues.calculateLeftPadding(layoutDirection),
                    end = 15.dp + paddingValues.calculateRightPadding(layoutDirection),
                    top = 5.dp + paddingValues.calculateTopPadding(),
                    bottom = 5.dp + paddingValues.calculateBottomPadding()
                )
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
                        requestLocationFun()
                    },
                    buttonTitle = stringResource(R.string.allow_location_access),
                    color = CustomColor.primaryColor700
                )
                Sizer(20)
                CustomTitleButton(
                    operation = {
                        userViewModel.getMyInfo()
                        nav?.navigate(Screens.PickCurrentAddress)
                    },
                    buttonTitle = stringResource(R.string.enter_location_manually),
                    color = CustomColor.primaryColor700
                )


            }

        }
    }

}



