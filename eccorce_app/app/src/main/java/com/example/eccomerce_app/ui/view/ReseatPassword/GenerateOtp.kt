package com.example.eccomerce_app.ui.view.ReseatPassword

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.ScrollState
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
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
import androidx.compose.ui.platform.LocalConfiguration
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


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun GenerateOtpScreen(
    nav: NavHostController, authViewModel: AuthViewModel
) {

    val context = LocalContext.current
    val layoutDirection = LocalLayoutDirection.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val coroutine = rememberCoroutineScope()

    val email = rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }
    val isLoading = rememberSaveable { mutableStateOf(false) }


    val snackBarHostState = remember { SnackbarHostState() }
    val activity = context as Activity
    val windowsSize = calculateWindowSizeClass(activity)

    val scrollState = rememberScrollState()

    fun checkEmail() {
        keyboardController?.hide()
        coroutine.launch {
            val result = async {
                authViewModel.getOtp(email.value.text, updateIsLoading = { value ->
                    isLoading.value = value
                })
            }.await()

            if (!result.isNullOrEmpty()) {
                snackBarHostState.showSnackbar(result)
            } else {
                nav.navigate(Screens.OtpVerification(email = email.value.text))
                email.value = TextFieldValue("")
            }
        }
    }


    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }) { paddingValues ->

        paddingValues.calculateTopPadding()
        paddingValues.calculateBottomPadding()


        when (windowsSize.widthSizeClass) {
            WindowWidthSizeClass.Compact -> CompactGenerateOtpLayout(
                contentPadding = paddingValues,
                email = email,
                isLoading = isLoading.value,
                onCheckClick = { checkEmail() },
                scrollState = scrollState,
                layoutDirection = layoutDirection,
                context= context
            )

            WindowWidthSizeClass.Medium, WindowWidthSizeClass.Expanded -> ExpandedGenerateOtpLayout(
                contentPadding = paddingValues,
                email = email,
                isLoading = isLoading.value,
                onCheckClick = { checkEmail() },
                scrollState = scrollState,
                layoutDirection = layoutDirection,
                context= context

            )
        }
    }
}

@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun CompactGenerateOtpLayout(
    contentPadding: PaddingValues,
    email: MutableState<TextFieldValue>,
    isLoading: Boolean,
    onCheckClick: () -> Unit,
    scrollState: ScrollState,
    layoutDirection: LayoutDirection,
    context: Activity
) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(
                start = 15.dp + contentPadding.calculateLeftPadding(layoutDirection),
                end = 15.dp + contentPadding.calculateRightPadding(layoutDirection),
                top = 5.dp + contentPadding.calculateTopPadding(),
                bottom = 5.dp + contentPadding.calculateBottomPadding()
            )
            .verticalScroll(scrollState)
            ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {


        Icon(
            ImageVector.vectorResource(R.drawable.mail_validation),
            "",
            tint = CustomColor.primaryColor700,
            modifier = Modifier.size(200.dp)
        )

        TextInputWithTitle(
            email,
            title = "",
            placeHolder = context.getString(R.string.enter_your_email),
        )

        CustomButton(
            isLoading = isLoading,
            operation = onCheckClick,
            isEnable = email.value.text.trim().isNotEmpty(),
            buttonTitle = stringResource(R.string.check),
        )
    }

}

@SuppressLint("ConfigurationScreenWidthHeight", "LocalContextGetResourceValueCall")
@Composable
fun ExpandedGenerateOtpLayout(
    contentPadding: PaddingValues,
    email: MutableState<TextFieldValue>,
    isLoading: Boolean,
    onCheckClick: () -> Unit,
    scrollState: ScrollState,
    layoutDirection: LayoutDirection,
    context: Activity
) {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp
    val screenHeight = config.screenHeightDp

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(
                start = 15.dp + contentPadding.calculateLeftPadding(layoutDirection),
                end = 15.dp + contentPadding.calculateRightPadding(layoutDirection),
                top = 5.dp + contentPadding.calculateTopPadding(),
                bottom = 5.dp + contentPadding.calculateBottomPadding()
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(((screenWidth / 3) - 20).dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.mail_validation),
                contentDescription = "",
                tint = CustomColor.primaryColor700,
                modifier = Modifier.size(200.dp)
            )
        }
      Sizer(width = 20)

        Column(
            modifier = Modifier
                .background(Color.White)
                .height(screenHeight.dp)
                .width(screenWidth.dp - (screenWidth.dp / 3) - 10.dp)

                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextInputWithTitle(
                email,
                title = "",
                placeHolder = context.getString(R.string.enter_your_email),
            )

            CustomButton(
                isLoading = isLoading,
                operation = onCheckClick,
                isEnable = email.value.text.trim().isNotEmpty(),
                buttonTitle = stringResource(R.string.check),
            )
        }

    }
}