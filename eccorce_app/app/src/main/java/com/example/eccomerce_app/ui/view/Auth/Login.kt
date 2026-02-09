package com.example.eccomerce_app.ui.view.Auth

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.foundation.*
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
import androidx.compose.ui.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.e_commercompose.R
import com.example.e_commercompose.ui.component.CustomAuthBottom
import com.example.e_commercompose.ui.theme.CustomColor
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
    val activity = context as Activity
    val sizeScreen = calculateWindowSizeClass(activity)

    val fontScall = LocalDensity.current.fontScale
    val keyboardController = LocalSoftwareKeyboardController.current

    val errorMessage = authKoin.errorMessage.collectAsStateWithLifecycle()

    val coroutine = rememberCoroutineScope()

    val snackBarHostState = remember { SnackbarHostState() }



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


        /* Box(modifier = Modifier.padding(bottom = 50.dp) )
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

         Column(
             modifier = Modifier
                 .fillMaxHeight(0.9f)
                 .fillMaxWidth()
                 .padding(top = 50.dp)
                 ,
             horizontalAlignment = Alignment.Start,
         )
         {



             Sizer(heigh = 50)
             TextInputWithTitle(
                 userNameOrEmail,
                 title = stringResource(R.string.email),
                 placeHolder = stringResource(R.string.enter_your_email),
                 errorMessage = errorMessageValidation.value,
                 isHasError = isEmailError.value,
             )
             TextSecureInputWithTitle(
                 password,
                 stringResource(R.string.password),
                 isPasswordError.value,
                 errorMessageValidation.value
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
                 isLoading = isSendingData.value,
                 operation = {
                     keyboardController?.hide()
                     if (userNameOrEmail.value.text.isBlank() || password.value.text.isBlank()) {
                         coroutine.launch {
                             snackBarHostState.showSnackbar(context.getString(R.string.user_name_or_password_is_blank))
                         }
                     } else {
                         coroutine.launch {
                             updateConditionValue(isSendingDataValue = true)

                             delay(10)

                             val token =
                                 async { authKoin.generateTokenNotification() }.await()

//                                Pair(
//                                    "fv6pNFrXSsC7o29xq991br:APA91bHiUFcyvxKKxcqWoPZzoIaeWEs6_uN36YI0II5HHpN3HP-dUQap9UbnPiyBB8Fc5xX6GiCYbDQ7HxuBlXZkAE2P0T82-DRQ160EiKCJ9tlPgfgQxa4",
//                                    null
//                                )

                             if (!token.first.isNullOrEmpty()) {
                                 val result = authKoin.loginUser(
                                     userNameOrEmail.value.text,
                                     password = password.value.text,
                                     token = token.first!!,
                                     updateStateLoading ={ value ->
                                         isSendingData.value = value
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

                             } else {
                                 updateConditionValue(isSendingDataValue = false)
                                 coroutine.launch {
                                     snackBarHostState.showSnackbar(
                                         token.second
                                             ?: context.getString(R.string.network_must_be_connected_to_complete_operation)
                                     )
                                 }
                             }
                         }

                     }
                 },
                 buttonTitle = stringResource(R.string.login),
                 validationFun = {
                     true
                     validateLoginInput(
                         username = userNameOrEmail.value.text, password = password.value.text
                     )
                 })
         }
     */

        when (sizeScreen.widthSizeClass) {
            WindowWidthSizeClass.Compact,
            WindowWidthSizeClass.Medium -> {
                CompactToMediumLayout(
                    nav = nav,
                    context = context,
                    authKoin = authKoin,
                    contentPadding = contentPadding,
                    fontScall = fontScall,
                    coroutine = coroutine,
                    keyboardController = keyboardController,
                    snackBarHostState = snackBarHostState

                )

            }

            WindowWidthSizeClass.Expanded -> {
                ExpandedLayout(
                    nav = nav,
                    context = context,
                    authKoin = authKoin,
                    contentPadding = contentPadding,
                    fontScall = fontScall,
                    coroutine = coroutine,
                    keyboardController = keyboardController,
                    snackBarHostState = snackBarHostState
                )
            }
        }

    }
}


@Composable
fun CompactToMediumLayout(
    nav: NavHostController,
    context: Context,
    authKoin: AuthViewModel,
    contentPadding: PaddingValues,
    fontScall: Float,
    coroutine: CoroutineScope,
    keyboardController: SoftwareKeyboardController?,
    snackBarHostState: SnackbarHostState,
) {


    val userNameOrEmail =
        rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("ali535@gmail.com")) }
    val password =
        rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("12AS@#fs")) }

    val errorMessageValidation = rememberSaveable { mutableStateOf("") }

    val isSendingData = rememberSaveable { mutableStateOf(false) }
    val isEmailError = rememberSaveable { mutableStateOf<Boolean?>(null) }


    fun updateConditionValue(isEmailErrorValue: Boolean = false) {
        when (isEmailErrorValue) {
            true -> isEmailError.value = true
            else -> isEmailError.value = false
        }
    }

    fun validateLoginInput(
        username: String, password: String
    ): Boolean {
        updateConditionValue(isEmailErrorValue = false)
        when {
            username.trim().isEmpty() -> {
                errorMessageValidation.value = context.getString(R.string.email_must_not_be_empty)
                updateConditionValue(isEmailErrorValue = true)
                return false
            }

            password.trim().isEmpty() -> {
                errorMessageValidation.value =
                    context.getString(R.string.password_must_not_be_empty)
                updateConditionValue(isEmailErrorValue = false)
                return false
            }

            else -> return true
        }
    }


    fun loginFun() {
        keyboardController?.hide()
        if (userNameOrEmail.value.text.isBlank() || password.value.text.isBlank()) {
            coroutine.launch {
                snackBarHostState.showSnackbar(context.getString(R.string.user_name_or_password_is_blank))
            }
        } else {
            coroutine.launch {

                delay(10)


                val result = authKoin.loginUser(
                    userNameOrEmail.value.text,
                    password = password.value.text,
                    updateStateLoading = { value ->
                        isSendingData.value = value
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


    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .padding(top = 50.dp)
            .padding(horizontal = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    )
    {


        Sizer(heigh = 50)
        TextInputWithTitle(
            userNameOrEmail,
            title = stringResource(R.string.email),
            placeHolder = stringResource(R.string.enter_your_email),
            errorMessage = errorMessageValidation.value,
            isHasError = isEmailError.value,
        )
        TextSecureInputWithTitle(
            password,
            stringResource(R.string.password),
            (isEmailError.value == false),
            errorMessageValidation.value
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
            isLoading = isSendingData.value,
            operation = {
                loginFun()
            },

            buttonTitle = stringResource(R.string.login),
            validationFun = {
                validateLoginInput(
                    userNameOrEmail.value.text,
                    password.value.text
                )
            })

        Box(
            modifier = Modifier.weight(1f)
        )
        Box(modifier = Modifier.padding(bottom = 50.dp + contentPadding.calculateBottomPadding()))
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
fun ExpandedLayout(
    nav: NavHostController,
    context: Context,
    authKoin: AuthViewModel,
    contentPadding: PaddingValues,
    fontScall: Float,
    coroutine: CoroutineScope,
    keyboardController: SoftwareKeyboardController?,
    snackBarHostState: SnackbarHostState,
) {

    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp
    val screenHeight = config.screenHeightDp
    val scrollState = rememberScrollState()

    val userNameOrEmail = rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("ali535@gmail.com"))
    }
    val password = rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("12AS@#fs"))
    }

    val errorMessageValidation = rememberSaveable { mutableStateOf("") }

    val isSendingData = rememberSaveable { mutableStateOf(false) }
    val isEmailError = rememberSaveable { mutableStateOf<Boolean?>(null) }


    fun updateConditionValue(isEmailErrorValue: Boolean = false) {
        when (isEmailErrorValue) {
            true -> isEmailError.value = true
            else -> isEmailError.value = false
        }
    }

    fun validateLoginInput(
        username: String, password: String
    ): Boolean {
        updateConditionValue(isEmailErrorValue = false)
        when {
            username.trim().isEmpty() -> {
                errorMessageValidation.value = context.getString(R.string.email_must_not_be_empty)
                updateConditionValue(isEmailErrorValue = true)
                return false
            }

            password.trim().isEmpty() -> {
                errorMessageValidation.value =
                    context.getString(R.string.password_must_not_be_empty)
                updateConditionValue(isEmailErrorValue = false)
                return false
            }

            else -> return true
        }
    }


    fun loginFun() {
        keyboardController?.hide()
        if (userNameOrEmail.value.text.isBlank() || password.value.text.isBlank()) {
            coroutine.launch {
                snackBarHostState.showSnackbar(context.getString(R.string.user_name_or_password_is_blank))
            }
        } else {
            coroutine.launch {

                delay(10)


                val result = authKoin.loginUser(
                    userNameOrEmail.value.text,
                    password = password.value.text,
                    updateStateLoading = { value ->
                        isSendingData.value = value
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


    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .width(((screenWidth / 2) - 20).dp)
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
                .padding(top = 50.dp)
                .padding(horizontal = 15.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {

            Box(modifier = Modifier.weight(1f))

            TextInputWithTitle(
                userNameOrEmail,
                title = stringResource(R.string.email),
                placeHolder = stringResource(R.string.enter_your_email),
                errorMessage = errorMessageValidation.value,
                isHasError = isEmailError.value,
            )
            TextSecureInputWithTitle(
                password,
                stringResource(R.string.password),
                (isEmailError.value == false),
                errorMessageValidation.value
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
                isLoading = isSendingData.value,
                operation = {
                    loginFun()
                },

                buttonTitle = stringResource(R.string.login),
                validationFun = {
                    validateLoginInput(
                        userNameOrEmail.value.text,
                        password.value.text
                    )
                })

            Box(
                modifier = Modifier.weight(1f)
            )
            Box(modifier = Modifier.padding(bottom = 10.dp + contentPadding.calculateBottomPadding()))
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