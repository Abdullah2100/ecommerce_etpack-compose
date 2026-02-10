package com.example.eccomerce_app.ui.view.Auth

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.eccomerce_app.viewModel.AuthViewModel
import com.example.eccomerce_app.util.General
import com.example.e_commercompose.ui.component.CustomAuthBottom
import com.example.eccomerce_app.ui.component.TextInputWithTitle
import com.example.eccomerce_app.ui.component.TextSecureInputWithTitle
import com.example.e_commercompose.ui.theme.CustomColor
import com.example.eccomerce_app.ui.Screens
import com.example.hotel_mobile.Util.Validation
import kotlinx.coroutines.launch
import com.example.e_commercompose.R
import com.example.eccomerce_app.ui.component.SharedAppBar
import com.example.eccomerce_app.ui.component.Sizer


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun SignUpPage(
    nav: NavHostController,
    authKoin: AuthViewModel
) {


    val errorMessage = authKoin.errorMessage.collectAsStateWithLifecycle()

    val snackBarHostState = remember { SnackbarHostState() }

    val coroutine = rememberCoroutineScope()

    val context = LocalContext.current
    val activity = context as Activity
    val windowsSize = calculateWindowSizeClass(activity)

    val keyboardController = LocalSoftwareKeyboardController.current



    LaunchedEffect(errorMessage.value) {
        if (errorMessage.value != null)
            coroutine.launch {
                snackBarHostState.showSnackbar(errorMessage.value.toString())
                authKoin.clearErrorMessage()
            }
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                modifier = Modifier.clip(RoundedCornerShape(8.dp))
            )
        },
        topBar = {
            SharedAppBar(title = stringResource(R.string.signup))
        },
    ) { contentPadding ->

        contentPadding.calculateTopPadding()
        contentPadding.calculateBottomPadding()

        when (windowsSize.widthSizeClass){
            WindowWidthSizeClass.Compact, WindowWidthSizeClass.Medium->
                CompactToMediumSignupLayout(nav,context,authKoin,contentPadding,keyboardController,snackBarHostState)
        WindowWidthSizeClass.Expanded->
            ExpandedSignupLayout(nav,context,authKoin,contentPadding,keyboardController,snackBarHostState)
        }


    }
}

@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun CompactToMediumSignupLayout(
    nav: NavHostController,
    context: Context,
    authKoin: AuthViewModel,
    contentPadding: PaddingValues,
    keyboardController: SoftwareKeyboardController?,
    snackBarHostState: SnackbarHostState,
) {

    val fontScall = LocalDensity.current.fontScale

    val scrollState = rememberScrollState()

    val coroutine = rememberCoroutineScope()




    val name = remember { mutableStateOf(TextFieldValue("slime")) }
    val email = remember { mutableStateOf(TextFieldValue("salime@gmail.com")) }
    val phone = remember { mutableStateOf(TextFieldValue("778537385")) }
    val password = remember { mutableStateOf(TextFieldValue("12AS@#fs")) }
    val confirmPassword = remember { mutableStateOf(TextFieldValue("12AS@#fs")) }


    val isLoading = rememberSaveable { mutableStateOf(false) }
    val isCheckBox = remember { mutableStateOf(false) }
    val isEmailError = remember { mutableStateOf(false) }
    val isNameError = remember { mutableStateOf(false) }
    val isPhoneError = remember { mutableStateOf(false) }
    val isPasswordError = remember { mutableStateOf(false) }
    val isPasswordConfirm = remember { mutableStateOf(false) }
    val isTermAndServicesError = remember { mutableStateOf(false) }
    val errorMessageValidation = remember { mutableStateOf("") }

    fun updateConditionValue(
        isLoadingValue: Boolean? = null,
        isCheckBoxValue: Boolean? = null,
        isEmailErrorValue: Boolean? = null,
        isNameErrorValue: Boolean? = null,
        isPhoneErrorValue: Boolean? = null,
        isPasswordErrorValue: Boolean? = null,
        isPasswordConfirmValue: Boolean? = null,
        isTermAndServicesErrorValue: Boolean? = null
    ) {
        when {
            isLoadingValue != null -> isLoading.value = isLoadingValue
            isCheckBoxValue != null -> isCheckBox.value = isCheckBoxValue
            isEmailErrorValue != null -> isEmailError.value = isEmailErrorValue
            isNameErrorValue != null -> isNameError.value = isNameErrorValue
            isPhoneErrorValue != null -> isPhoneError.value = isPhoneErrorValue
            isPasswordErrorValue != null -> isPasswordError.value = isPasswordErrorValue
            isPasswordConfirmValue != null -> isPasswordConfirm.value = isPasswordConfirmValue
            isTermAndServicesErrorValue != null -> isTermAndServicesError.value =
                isTermAndServicesErrorValue
        }
    }

    fun validateSignupInput(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {

        updateConditionValue(
            isNameErrorValue = false,
            isEmailErrorValue = false,
            isPasswordErrorValue = false,
            isPasswordConfirmValue = false
        )

        when {

            name.trim().isEmpty() -> {
                errorMessageValidation.value = context.getString(R.string.name_must_not_be_empty)
                updateConditionValue(isNameErrorValue = true)
                return false
            }

            email.trim().isEmpty() -> {
                errorMessageValidation.value = context.getString(R.string.email_must_not_be_empty)
                updateConditionValue(isEmailErrorValue = true)
                return false
            }

            !Validation.emailValidation(email) -> {
                errorMessageValidation.value = context.getString(R.string.write_valid_email)
                updateConditionValue(isEmailErrorValue = true)
                return false
            }

            phone.value.text.trim().isEmpty() -> {
                errorMessageValidation.value = context.getString(R.string.phone_must_not_empty)
                updateConditionValue(isPhoneErrorValue = true)
                return false
            }

            password.trim().isEmpty() -> {
                errorMessageValidation.value =
                    (context.getString(R.string.password_must_not_be_empty))
                updateConditionValue(isPasswordErrorValue = true)
                return false
            }

            !Validation.passwordSmallValidation(password) -> {
                errorMessageValidation.value =
                    (context.getString(R.string.password_must_not_contain_two_small_letter))
                updateConditionValue(isPasswordErrorValue = true)
                return false
            }

            !Validation.passwordNumberValidation(password) -> {
                errorMessageValidation.value =
                    (context.getString(R.string.password_must_not_contain_two_number))
                updateConditionValue(isPasswordErrorValue = true)
                return false
            }

            !Validation.passwordCapitalValidation(password) -> {
                errorMessageValidation.value =
                    (context.getString(R.string.password_must_not_contain_two_capitalletter))
                updateConditionValue(isPasswordErrorValue = true)
                return false
            }

            !Validation.passwordSpicialCharracterValidation(password) -> {
                errorMessageValidation.value =
                    (context.getString(R.string.password_must_not_contain_two_spical_character))
                updateConditionValue(isPasswordErrorValue = true)
                return false
            }

            confirmPassword.trim().isEmpty() -> {
                errorMessageValidation.value =
                    context.getString(R.string.password_must_not_be_empty)
                updateConditionValue(isPasswordConfirmValue = true)
                return false
            }

            password != confirmPassword -> {
                errorMessageValidation.value =
                    (context.getString(R.string.confirm_password_not_equal_to_password))
                updateConditionValue(isPasswordConfirmValue = true)
                return false
            }

            !isCheckBox.value -> {
                errorMessageValidation.value =
                    context.getString(R.string.term_and_policies_is_required)
                updateConditionValue(isTermAndServicesErrorValue = true)

                return false
            }


            else -> return true
        }

    }

    fun signUp() {

        coroutine.launch {
            keyboardController?.hide()
            updateConditionValue(isLoadingValue = true)


            val result = authKoin.signUpUser(
                phone = phone.value.text,
                email = email.value.text,
                password = password.value.text,
                name = name.value.text,
                updateIsLoading = { value -> isLoading.value = value }
            )

            updateConditionValue(isLoadingValue = false)


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


    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(
                top = contentPadding.calculateTopPadding(),
                bottom = contentPadding.calculateBottomPadding(),
                start = 15.dp,
                end = 15.dp)

            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start,
    )
    {

        TextInputWithTitle(
            name,
            title = stringResource(R.string.name),
            placeHolder = stringResource(R.string.enter_your_name),
            errorMessage = errorMessageValidation.value,
            isHasError = isNameError.value,
        )

        TextInputWithTitle(
            email,
            title = stringResource(R.string.email),
            placeHolder = stringResource(R.string.enter_your_email),
            errorMessage = errorMessageValidation.value,
            isHasError = isEmailError.value,
        )
        TextInputWithTitle(
            phone,
            title = stringResource(R.string.phone),
            placeHolder = stringResource(R.string.enter_phone),
            errorMessage = errorMessageValidation.value,
            isHasError = isPhoneError.value,
        )
        TextSecureInputWithTitle(
            password,
            stringResource(R.string.password),
            isPasswordError.value,
            errorMessageValidation.value,
        )
        TextSecureInputWithTitle(
            confirmPassword,
            stringResource(R.string.confirm_password),
            isPasswordConfirm.value,
            errorMessageValidation.value,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-10).dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Checkbox(
                checked = isCheckBox.value,
                onCheckedChange = { updateConditionValue(isCheckBoxValue = !isCheckBox.value) },
                colors = CheckboxDefaults.colors(
                    checkedColor = CustomColor.primaryColor700
                ),
                modifier = Modifier.padding()
            )
            Text(
                stringResource(R.string.agree_with),
                fontFamily = General.satoshiFamily,
                fontWeight = FontWeight.Normal,
                color = if (isTermAndServicesError.value) CustomColor.alertColor_1_400 else CustomColor.neutralColor950,
                fontSize = (16 / fontScall).sp,
            )
            Text(
                stringResource(R.string.term_condition),
                fontFamily = General.satoshiFamily,
                fontWeight = FontWeight.Medium,
                color = if (isTermAndServicesError.value) CustomColor.alertColor_1_400 else CustomColor.primaryColor700,
                fontSize = (16 / fontScall).sp,
                modifier = Modifier
                    .padding(start = 3.dp),
                textDecoration = TextDecoration.Underline
            )
        }
        if (isTermAndServicesError.value)
            Text(
                errorMessageValidation.value,
                color = CustomColor.alertColor_1_400,
                fontFamily = General.satoshiFamily,
                fontWeight = FontWeight.Medium,
                fontSize = (14 / fontScall).sp,
                modifier = Modifier
                    .offset(x = (13).dp, y = (-12).dp)
            )

        CustomAuthBottom(
            isLoading = isLoading.value,
            validationFun = {
                validateSignupInput(
                    email = email.value.text,
                    name = name.value.text,
                    password = password.value.text,
                    confirmPassword = confirmPassword.value.text
                )
            },
            buttonTitle = stringResource(R.string.signup),
            operation = { signUp() }
        )
    }

}


@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun ExpandedSignupLayout(
    nav: NavHostController,
    context: Context,
    authKoin: AuthViewModel,
    contentPadding: PaddingValues,
    keyboardController: SoftwareKeyboardController?,
    snackBarHostState: SnackbarHostState,
) {

    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp
    val scrollState = rememberScrollState()

    val fontScall = LocalDensity.current.fontScale


    val coroutine = rememberCoroutineScope()


    val name = remember { mutableStateOf(TextFieldValue("slime")) }
    val email = remember { mutableStateOf(TextFieldValue("salime@gmail.com")) }
    val phone = remember { mutableStateOf(TextFieldValue("778537385")) }
    val password = remember { mutableStateOf(TextFieldValue("12AS@#fs")) }
    val confirmPassword = remember { mutableStateOf(TextFieldValue("12AS@#fs")) }


    val isLoading = rememberSaveable { mutableStateOf(false) }
    val isCheckBox = remember { mutableStateOf(false) }
    val isEmailError = remember { mutableStateOf(false) }
    val isNameError = remember { mutableStateOf(false) }
    val isPhoneError = remember { mutableStateOf(false) }
    val isPasswordError = remember { mutableStateOf(false) }
    val isPasswordConfirm = remember { mutableStateOf(false) }
    val isTermAndServicesError = remember { mutableStateOf(false) }
    val errorMessageValidation = remember { mutableStateOf("") }

    fun updateConditionValue(
        isLoadingValue: Boolean? = null,
        isCheckBoxValue: Boolean? = null,
        isEmailErrorValue: Boolean? = null,
        isNameErrorValue: Boolean? = null,
        isPhoneErrorValue: Boolean? = null,
        isPasswordErrorValue: Boolean? = null,
        isPasswordConfirmValue: Boolean? = null,
        isTermAndServicesErrorValue: Boolean? = null
    ) {
        when {
            isLoadingValue != null -> isLoading.value = isLoadingValue
            isCheckBoxValue != null -> isCheckBox.value = isCheckBoxValue
            isEmailErrorValue != null -> isEmailError.value = isEmailErrorValue
            isNameErrorValue != null -> isNameError.value = isNameErrorValue
            isPhoneErrorValue != null -> isPhoneError.value = isPhoneErrorValue
            isPasswordErrorValue != null -> isPasswordError.value = isPasswordErrorValue
            isPasswordConfirmValue != null -> isPasswordConfirm.value = isPasswordConfirmValue
            isTermAndServicesErrorValue != null -> isTermAndServicesError.value =
                isTermAndServicesErrorValue
        }
    }

    fun validateSignupInput(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {

        updateConditionValue(
            isNameErrorValue = false,
            isEmailErrorValue = false,
            isPasswordErrorValue = false,
            isPasswordConfirmValue = false
        )

        when {

            name.trim().isEmpty() -> {
                errorMessageValidation.value = context.getString(R.string.name_must_not_be_empty)
                updateConditionValue(isNameErrorValue = true)
                return false
            }

            email.trim().isEmpty() -> {
                errorMessageValidation.value = context.getString(R.string.email_must_not_be_empty)
                updateConditionValue(isEmailErrorValue = true)
                return false
            }

            !Validation.emailValidation(email) -> {
                errorMessageValidation.value = context.getString(R.string.write_valid_email)
                updateConditionValue(isEmailErrorValue = true)
                return false
            }

            phone.value.text.trim().isEmpty() -> {
                errorMessageValidation.value = context.getString(R.string.phone_must_not_empty)
                updateConditionValue(isPhoneErrorValue = true)
                return false
            }

            password.trim().isEmpty() -> {
                errorMessageValidation.value =
                    (context.getString(R.string.password_must_not_be_empty))
                updateConditionValue(isPasswordErrorValue = true)
                return false
            }

            !Validation.passwordSmallValidation(password) -> {
                errorMessageValidation.value =
                    (context.getString(R.string.password_must_not_contain_two_small_letter))
                updateConditionValue(isPasswordErrorValue = true)
                return false
            }

            !Validation.passwordNumberValidation(password) -> {
                errorMessageValidation.value =
                    (context.getString(R.string.password_must_not_contain_two_number))
                updateConditionValue(isPasswordErrorValue = true)
                return false
            }

            !Validation.passwordCapitalValidation(password) -> {
                errorMessageValidation.value =
                    (context.getString(R.string.password_must_not_contain_two_capitalletter))
                updateConditionValue(isPasswordErrorValue = true)
                return false
            }

            !Validation.passwordSpicialCharracterValidation(password) -> {
                errorMessageValidation.value =
                    (context.getString(R.string.password_must_not_contain_two_spical_character))
                updateConditionValue(isPasswordErrorValue = true)
                return false
            }

            confirmPassword.trim().isEmpty() -> {
                errorMessageValidation.value =
                    context.getString(R.string.password_must_not_be_empty)
                updateConditionValue(isPasswordConfirmValue = true)
                return false
            }

            password != confirmPassword -> {
                errorMessageValidation.value =
                    (context.getString(R.string.confirm_password_not_equal_to_password))
                updateConditionValue(isPasswordConfirmValue = true)
                return false
            }

            !isCheckBox.value -> {
                errorMessageValidation.value =
                    context.getString(R.string.term_and_policies_is_required)
                updateConditionValue(isTermAndServicesErrorValue = true)

                return false
            }


            else -> return true
        }

    }


    fun signUp() {

        coroutine.launch {
            keyboardController?.hide()
            updateConditionValue(isLoadingValue = true)


            val result = authKoin.signUpUser(
                phone = phone.value.text,
                email = email.value.text,
                password = password.value.text,
                name = name.value.text,
                updateIsLoading = { value -> isLoading.value = value }
            )

            updateConditionValue(isLoadingValue = false)


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
                .padding(top = contentPadding.calculateTopPadding(), bottom = contentPadding.calculateBottomPadding())
                .padding(horizontal = 15.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        )
        {


            TextInputWithTitle(
                name,
                title = stringResource(R.string.name),
                placeHolder = stringResource(R.string.enter_your_name),
                errorMessage = errorMessageValidation.value,
                isHasError = isNameError.value,
            )

            TextInputWithTitle(
                email,
                title = stringResource(R.string.email),
                placeHolder = stringResource(R.string.enter_your_email),
                errorMessage = errorMessageValidation.value,
                isHasError = isEmailError.value,
            )
            TextInputWithTitle(
                phone,
                title = stringResource(R.string.phone),
                placeHolder = stringResource(R.string.enter_phone),
                errorMessage = errorMessageValidation.value,
                isHasError = isPhoneError.value,
            )
            TextSecureInputWithTitle(
                password,
                stringResource(R.string.password),
                isPasswordError.value,
                errorMessageValidation.value,
            )
            TextSecureInputWithTitle(
                confirmPassword,
                stringResource(R.string.confirm_password),
                isPasswordConfirm.value,
                errorMessageValidation.value,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-10).dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Checkbox(
                    checked = isCheckBox.value,
                    onCheckedChange = { updateConditionValue(isCheckBoxValue = !isCheckBox.value) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = CustomColor.primaryColor700
                    ),
                    modifier = Modifier.padding()
                )
                Text(
                    stringResource(R.string.agree_with),
                    fontFamily = General.satoshiFamily,
                    fontWeight = FontWeight.Normal,
                    color = if (isTermAndServicesError.value) CustomColor.alertColor_1_400 else CustomColor.neutralColor950,
                    fontSize = (16 / fontScall).sp,
                )
                Text(
                    stringResource(R.string.term_condition),
                    fontFamily = General.satoshiFamily,
                    fontWeight = FontWeight.Medium,
                    color = if (isTermAndServicesError.value) CustomColor.alertColor_1_400 else CustomColor.primaryColor700,
                    fontSize = (16 / fontScall).sp,
                    modifier = Modifier
                        .padding(start = 3.dp),
                    textDecoration = TextDecoration.Underline
                )
            }
            if (isTermAndServicesError.value)
                Text(
                    errorMessageValidation.value,
                    color = CustomColor.alertColor_1_400,
                    fontFamily = General.satoshiFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = (14 / fontScall).sp,
                    modifier = Modifier
                        .offset(x = (13).dp, y = (-12).dp)
                )

            CustomAuthBottom(
                isLoading = isLoading.value,
                validationFun = {
                    validateSignupInput(
                        email = email.value.text,
                        name = name.value.text,
                        password = password.value.text,
                        confirmPassword = confirmPassword.value.text
                    )
                },
                buttonTitle = stringResource(R.string.signup),
                operation = {signUp() }
            )
        }

    }
}