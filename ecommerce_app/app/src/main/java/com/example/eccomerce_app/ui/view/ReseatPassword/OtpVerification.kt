package com.example.eccomerce_app.ui.view.ReseatPassword

import android.annotation.SuppressLint
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.e_commercompose.R
import com.example.e_commercompose.ui.component.CustomButton
import com.example.e_commercompose.ui.theme.CustomColor
import com.example.eccomerce_app.ui.Screens
import com.example.eccomerce_app.ui.component.Sizer
import com.example.eccomerce_app.ui.component.TextInputWithTitle
import com.example.eccomerce_app.viewModel.AuthViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun OtpVerificationScreen(
    nav: NavHostController,
    authViewModel: AuthViewModel,
    email: String
) {


    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val layoutDirection = LocalLayoutDirection.current
    val coroutine = rememberCoroutineScope()

    val snackBarHostState = remember { SnackbarHostState() }

    val otpValue = rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }


    val isSendingData = rememberSaveable { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    fun verifyOtp() {
        keyboardController?.hide()
        coroutine.launch {
            val result = async {
                authViewModel.otpVerifying(email, otpValue.value.text, updateIsLoading = { value ->
                    isSendingData.value = value
                })
            }.await()
            if (!result.isNullOrEmpty()) {
                snackBarHostState.showSnackbar(result)
            } else {
                nav.navigate(Screens.ReseatPassword(email, otpValue.value.text))
            }
        }
    }


    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(
                    start = 15.dp + paddingValues.calculateLeftPadding(layoutDirection),
                    end = 15.dp + paddingValues.calculateRightPadding(layoutDirection),
                    top = 5.dp + paddingValues.calculateTopPadding(),
                    bottom = 5.dp + paddingValues.calculateBottomPadding()
                )
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                ImageVector.vectorResource(R.drawable.otp_verification),
                "",
                tint = CustomColor.primaryColor700,
                modifier = Modifier.size(200.dp)
            )
            Sizer(heigh = 5)

            TextInputWithTitle(
                onChange = {value-> otpValue.value = TextFieldValue(value)},
                title = "",
                placeHolder = stringResource(R.string.enter_your_otp),
            )


            CustomButton(
                isLoading = isSendingData.value,
                operation = { verifyOtp() },
                isEnable = otpValue.value.text.trim().isNotEmpty(),
                buttonTitle = stringResource(R.string.verifying),

                )

        }
    }

}