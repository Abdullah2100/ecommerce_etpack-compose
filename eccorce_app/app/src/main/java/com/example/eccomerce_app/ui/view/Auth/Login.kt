package com.example.eccomerce_app.ui.view.Auth

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.*
import androidx.lifecycle.compose.*
import androidx.navigation.NavHostController
import com.example.eccomerce_app.viewModel.AuthViewModel
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.e_commercompose.R
import com.example.e_commercompose.ui.component.CustomAuthBottom
import com.example.e_commercompose.ui.theme.CustomColor
import com.example.eccomerce_app.model.LoginScreenDataHolder
import com.example.eccomerce_app.ui.Screens
import com.example.eccomerce_app.ui.component.SharedAppBar
import com.example.eccomerce_app.ui.component.Sizer
import com.example.eccomerce_app.ui.component.TextInputWithTitle
import com.example.eccomerce_app.ui.component.TextSecureInputWithTitle
import com.example.eccomerce_app.util.General
import kotlinx.coroutines.*


@SuppressLint("LocalContextGetResourceValueCall")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun LoginScreen(
    nav: NavHostController,
    authKoin: AuthViewModel
) {
    val context = LocalContext.current
    val layoutDirection = LocalLayoutDirection.current

    val activity = context as Activity
    val sizeScreen = calculateWindowSizeClass(activity)

    val fontScall = LocalDensity.current.fontScale
    val keyboardController = LocalSoftwareKeyboardController.current

    val errorMessage = authKoin.errorMessage.collectAsStateWithLifecycle()

    val coroutine = rememberCoroutineScope()

    val scrollState = rememberScrollState()

    val snackBarHostState = remember { SnackbarHostState() }

    val state = LoginScreenDataHolder(
        userNameOrEmail = rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(
                TextFieldValue("ali535@gmail.com")
            )
        },
        password = rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(
                TextFieldValue("12AS@#fs")
            )
        },
        errorMessageValidation = rememberSaveable() { mutableStateOf("") },
        isSendingData = rememberSaveable { mutableStateOf(false) },
        isEmailError = rememberSaveable { mutableStateOf<Boolean?>(null) },
    )


    fun updateConditionValue(isEmailErrorValue: Boolean = false) {
        when (isEmailErrorValue) {
            true -> state.isEmailError.value = true
            else -> state.isEmailError.value = false
        }
    }

    fun isValidateInput(
        username: String, password: String
    ): Boolean {
        when {
            username.trim().isEmpty() -> {
                state.errorMessageValidation.value =
                    context.getString(R.string.email_must_not_be_empty)
                updateConditionValue(isEmailErrorValue = true)
                return false
            }

            password.trim().isEmpty() -> {
                state.errorMessageValidation.value =
                    context.getString(R.string.password_must_not_be_empty)
                updateConditionValue(isEmailErrorValue = false)
                return false
            }

            else -> return true
        }
    }


    fun loginFun() {
        keyboardController?.hide()
        coroutine.launch {
        if (state.userNameOrEmail.value.text.isBlank() || state.password.value.text.isBlank()) {
                snackBarHostState.showSnackbar(context.getString(R.string.user_name_or_password_is_blank))

        } else {

                delay(10)

                val result = authKoin.loginUser(
                    state.userNameOrEmail.value.text,
                    password = state.password.value.text,
                    updateStateLoading = { value ->
                        state.isSendingData.value = value
                    },
                )

                if (result.isNullOrEmpty())
                    nav.navigate(Screens.LocationGraph) {
                        popUpTo(nav.graph.id) {
                            inclusive = true
                        }
                    }
                else
                    snackBarHostState.showSnackbar(result)

            }


        }
    }


    LaunchedEffect(errorMessage.value) {
        if (errorMessage.value != null) coroutine.launch {
            snackBarHostState.showSnackbar(errorMessage.value.toString())
            authKoin.clearErrorMessage()
        }
    }

    Scaffold(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize(),
        topBar = { SharedAppBar(title = stringResource(R.string.login)) },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    )
    { contentPadding ->
        contentPadding.calculateTopPadding()
        contentPadding.calculateBottomPadding()

        when (sizeScreen.widthSizeClass) {
            WindowWidthSizeClass.Compact,
            WindowWidthSizeClass.Medium -> {
                CompactToMediumLoginLayout(
                    nav = nav,
                    contentPadding = contentPadding,
                    fontScall = fontScall,
                    state=state,
                    scrollState= scrollState,
                    isValidateInput = {isValidateInput(state.userNameOrEmail.value.text,state.password.value.text)},
                    loginFun = {loginFun()},
                    layoutDirection = layoutDirection
                    )

            }

            WindowWidthSizeClass.Expanded -> {
                ExpandedLoginLayout(
                    nav = nav,
                    contentPadding = contentPadding,
                    fontScall = fontScall,
                    state=state,
                    scrollState= scrollState,
                    isValidateInput = {isValidateInput(state.userNameOrEmail.value.text,state.password.value.text)},
                    loginFun = {loginFun()},
                    layoutDirection = layoutDirection
                )
            }
        }

    }
}


@Composable
fun CompactToMediumLoginLayout(
    nav: NavHostController,
    contentPadding: PaddingValues,
    fontScall: Float,
    state: LoginScreenDataHolder,
    isValidateInput: () -> Boolean,
    loginFun: () -> Unit,
    scrollState: ScrollState,
    layoutDirection: LayoutDirection
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(
                start = 15.dp + contentPadding.calculateLeftPadding(layoutDirection),
                end = 15.dp + contentPadding.calculateRightPadding(layoutDirection),
                top = 5.dp+contentPadding.calculateTopPadding(),
                bottom = 5.dp+contentPadding.calculateBottomPadding()
            )
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    )
    {


        Sizer(heigh = 50)
        TextInputWithTitle(
            state.userNameOrEmail,
            title = stringResource(R.string.email),
            placeHolder = stringResource(R.string.enter_your_email),
            errorMessage = state.errorMessageValidation.value,
            isHasError = state.isEmailError.value,
        )
        TextSecureInputWithTitle(
            state.password,
            stringResource(R.string.password),
            (state.isEmailError.value == false),
            state.errorMessageValidation.value
        )
        Box(
            modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                stringResource(R.string.forget_password),
                fontFamily = General.satoshiFamily,
                fontWeight = FontWeight.Medium,
                color = CustomColor.neutralColor950,
                fontSize = (16 / fontScall).sp,
                modifier = Modifier.clickable {
                    nav.navigate(Screens.ReseatPasswordGraph)
                })
        }

        Sizer(heigh = 30)


        CustomAuthBottom(
            isLoading = state.isSendingData.value,
            operation = {
                loginFun()
            },

            buttonTitle = stringResource(R.string.login),
            validationFun = {
                isValidateInput()
            })

        Box(
            modifier = Modifier.weight(1f)
        )
        Box()
        {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.don_t_have_any_account_yet),
                    color = CustomColor.neutralColor800,
                    fontFamily = General.satoshiFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = (16 / fontScall).sp
                )
                Box(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .clickable {
                            nav.navigate(Screens.Signup)
                        }) {
                    Text(
                        text = stringResource(R.string.signup),
                        color = CustomColor.primaryColor700,
                        fontFamily = General.satoshiFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = (16 / fontScall).sp

                    )

                }
            }
        }

    }
}


@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun ExpandedLoginLayout(
    nav: NavHostController,
    contentPadding: PaddingValues,
    fontScall: Float,
    state: LoginScreenDataHolder,
    isValidateInput: () -> Boolean,
    loginFun: () -> Unit,
    scrollState: ScrollState,
    layoutDirection: LayoutDirection
) {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp




    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(
                start = 15.dp + contentPadding.calculateLeftPadding(layoutDirection),
                end = 15.dp + contentPadding.calculateRightPadding(layoutDirection),
                top = 5.dp+contentPadding.calculateTopPadding(),
                bottom = 5.dp+contentPadding.calculateBottomPadding()
            )
    ) {
        Box(
            modifier = Modifier
                .width(((screenWidth / 3) - 20).dp)
                .padding(
                    top = contentPadding.calculateTopPadding(),
                    bottom = contentPadding.calculateBottomPadding()
                )
        ) {
            Image(
                painter = painterResource(R.drawable.login_icon),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxHeight()
            )
        }
        Sizer(width = 20)

        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxHeight(1f)
                .fillMaxWidth()
                .padding(
                    top = contentPadding.calculateTopPadding(),
                    bottom = contentPadding.calculateBottomPadding()
                )
                .padding(horizontal = 15.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {

            Box(modifier = Modifier.weight(1f))

            TextInputWithTitle(
                state.userNameOrEmail,
                title = stringResource(R.string.email),
                placeHolder = stringResource(R.string.enter_your_email),
                errorMessage = state.errorMessageValidation.value,
                isHasError = state.isEmailError.value,
            )
            TextSecureInputWithTitle(
                state.password,
                stringResource(R.string.password),
                (state.isEmailError.value == false),
                state.errorMessageValidation.value
            )
            Box(
                modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    stringResource(R.string.forget_password),
                    fontFamily = General.satoshiFamily,
                    fontWeight = FontWeight.Medium,
                    color = CustomColor.neutralColor950,
                    fontSize = (12 / fontScall).sp,
                    modifier = Modifier.clickable {
                        nav.navigate(Screens.ReseatPasswordGraph)
                    })
            }

            Sizer(heigh = 30)


            CustomAuthBottom(
                isLoading = state.isSendingData.value,
                operation = {
                    loginFun()
                },

                buttonTitle = stringResource(R.string.login),
                validationFun = {
                    isValidateInput()
                })

            Box(
                modifier = Modifier.weight(1f)
            )
            Box(modifier = Modifier.padding(bottom = 5.dp + contentPadding.calculateBottomPadding()))
            {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.don_t_have_any_account_yet),
                        color = CustomColor.neutralColor800,
                        fontFamily = General.satoshiFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = (16 / fontScall).sp
                    )
                    Box(
                        modifier = Modifier
                            .padding(start = 5.dp)
                            .clickable {
                                nav.navigate(Screens.Signup)
                            }) {
                        Text(
                            text = stringResource(R.string.signup),
                            color = CustomColor.primaryColor700,
                            fontFamily = General.satoshiFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = (16 / fontScall).sp

                        )

                    }
                }
            }

        }
    }
}