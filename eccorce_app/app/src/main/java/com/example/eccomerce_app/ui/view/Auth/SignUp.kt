package com.example.eccomerce_app.ui.view.Auth

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.eccomerce_app.model.SignupScreenDataHolder
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
    val layoutDirection = LocalLayoutDirection.current

    val activity = context as Activity
    val windowsSize = calculateWindowSizeClass(activity)
    val scrollState = rememberScrollState()
    val fontScall = LocalDensity.current.fontScale

    val keyboardController = LocalSoftwareKeyboardController.current


    val dataHolder = SignupScreenDataHolder(

        name = rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("slime")) },
        email = rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(
                TextFieldValue(
                    "salime@gmail.com"
                )
            )
        },
        phone = rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(
                TextFieldValue(
                    "778537385"
                )
            )
        },
        password = rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(
                TextFieldValue("12AS@#fs")
            )
        },
        confirmPassword = rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(
                TextFieldValue("12AS@#fs")
            )
        },
        isLoading = rememberSaveable { mutableStateOf(false) },
        isCheckBox = rememberSaveable { mutableStateOf(false) },
        isNameError = rememberSaveable { mutableStateOf(false) },
        isEmailError = rememberSaveable { mutableStateOf(false) },
        isPhoneError = rememberSaveable { mutableStateOf(false) },
        isPasswordError = rememberSaveable { mutableStateOf(false) },
        isPasswordConfirm = rememberSaveable { mutableStateOf(false) },
        isTermAndServicesError = rememberSaveable { mutableStateOf(false) },

        errorMessageValidation = rememberSaveable { mutableStateOf("") }

    )

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
            isLoadingValue != null -> dataHolder.isLoading.value = isLoadingValue
            isCheckBoxValue != null -> dataHolder.isCheckBox.value = isCheckBoxValue
            isEmailErrorValue != null -> dataHolder.isEmailError.value = isEmailErrorValue
            isNameErrorValue != null -> dataHolder.isNameError.value = isNameErrorValue
            isPhoneErrorValue != null -> dataHolder.isPhoneError.value = isPhoneErrorValue
            isPasswordErrorValue != null -> dataHolder.isPasswordError.value = isPasswordErrorValue
            isPasswordConfirmValue != null -> dataHolder.isPasswordConfirm.value =
                isPasswordConfirmValue

            isTermAndServicesErrorValue != null -> dataHolder.isTermAndServicesError.value =
                isTermAndServicesErrorValue
        }
    }

    fun validateSignupInput(): Boolean {

        updateConditionValue(
            isNameErrorValue = false,
            isEmailErrorValue = false,
            isPasswordErrorValue = false,
            isPasswordConfirmValue = false
        )

        when {

            dataHolder.name.value.text.trim().isEmpty() -> {
                dataHolder.errorMessageValidation.value =
                    context.getString(R.string.name_must_not_be_empty)
                updateConditionValue(isNameErrorValue = true)
                return false
            }

            dataHolder.email.value.text.trim().isEmpty() -> {
                dataHolder.errorMessageValidation.value =
                    context.getString(R.string.email_must_not_be_empty)
                updateConditionValue(isEmailErrorValue = true)
                return false
            }

            !Validation.emailValidation(dataHolder.email.value.text) -> {
                dataHolder.errorMessageValidation.value =
                    context.getString(R.string.write_valid_email)
                updateConditionValue(isEmailErrorValue = true)
                return false
            }

            dataHolder.phone.value.text.trim().isEmpty() -> {
                dataHolder.errorMessageValidation.value =
                    context.getString(R.string.phone_must_not_empty)
                updateConditionValue(isPhoneErrorValue = true)
                return false
            }

            dataHolder.password.value.text.trim().isEmpty() -> {
                dataHolder.errorMessageValidation.value =
                    (context.getString(R.string.password_must_not_be_empty))
                updateConditionValue(isPasswordErrorValue = true)
                return false
            }

            !Validation.passwordSmallValidation(dataHolder.password.value.text) -> {
                dataHolder.errorMessageValidation.value =
                    (context.getString(R.string.password_must_not_contain_two_small_letter))
                updateConditionValue(isPasswordErrorValue = true)
                return false
            }

            !Validation.passwordNumberValidation(dataHolder.password.value.text) -> {
                dataHolder.errorMessageValidation.value =
                    (context.getString(R.string.password_must_not_contain_two_number))
                updateConditionValue(isPasswordErrorValue = true)
                return false
            }

            !Validation.passwordCapitalValidation(dataHolder.password.value.text) -> {
                dataHolder.errorMessageValidation.value =
                    (context.getString(R.string.password_must_not_contain_two_capitalletter))
                updateConditionValue(isPasswordErrorValue = true)
                return false
            }

            !Validation.passwordSpicialCharracterValidation(dataHolder.password.value.text) -> {
                dataHolder.errorMessageValidation.value =
                    (context.getString(R.string.password_must_not_contain_two_spical_character))
                updateConditionValue(isPasswordErrorValue = true)
                return false
            }

            dataHolder.confirmPassword.value.text.trim().isEmpty() -> {
                dataHolder.errorMessageValidation.value =
                    context.getString(R.string.password_must_not_be_empty)
                updateConditionValue(isPasswordConfirmValue = true)
                return false
            }

            dataHolder.password.value.text != dataHolder.confirmPassword.value.text -> {
                dataHolder.errorMessageValidation.value =
                    (context.getString(R.string.confirm_password_not_equal_to_password))
                updateConditionValue(isPasswordConfirmValue = true)
                return false
            }

            !dataHolder.isCheckBox.value -> {
                dataHolder.errorMessageValidation.value =
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
                phone = dataHolder.phone.value.text,
                email = dataHolder.email.value.text,
                password = dataHolder.password.value.text,
                name = dataHolder.name.value.text,
                updateIsLoading = { value -> dataHolder.isLoading.value = value }
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
            SharedAppBar(
                title = stringResource(R.string.signup),
                nav = nav
            )
        },
    ) { contentPadding ->

        contentPadding.calculateTopPadding()
        contentPadding.calculateBottomPadding()

        when (windowsSize.widthSizeClass) {
            WindowWidthSizeClass.Compact, WindowWidthSizeClass.Medium ->
                CompactToMediumSignupLayout(
                    contentPadding,
                    dataHolder,
                    scrollState,
                    fontScall,
                    layoutDirection,
                    validateInput = { validateSignupInput() },
                    updateClickPolicesAndTerm = { value -> updateConditionValue(isCheckBoxValue = value) },
                    signupFun = { signUp() }
                )

            WindowWidthSizeClass.Expanded ->
                ExpandedSignupLayout(
                    contentPadding,
                    dataHolder,
                    scrollState,
                    fontScall,
                    layoutDirection,
                    validateInput = { validateSignupInput() },
                    updateClickPolicesAndTerm = { value -> updateConditionValue(isCheckBoxValue = value) },
                    signupFun = { signUp() }
                )
        }


    }
}

@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun CompactToMediumSignupLayout(
    contentPadding: PaddingValues,
    dataHolder: SignupScreenDataHolder,
    scrollState: ScrollState,
    fontScall: Float,
    layoutDirection: LayoutDirection,
    validateInput: () -> Boolean,
    updateClickPolicesAndTerm: (state: Boolean) -> Unit,
    signupFun: () -> Unit
) {


    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(
                start = 15.dp + contentPadding.calculateLeftPadding(layoutDirection),
                end = 15.dp + contentPadding.calculateRightPadding(layoutDirection),
                top = 5.dp+contentPadding.calculateTopPadding(),
                bottom = 5.dp+contentPadding.calculateBottomPadding()

            )

            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start,
    )
    {

        TextInputWithTitle(
            dataHolder.name,
            title = stringResource(R.string.name),
            placeHolder = stringResource(R.string.enter_your_name),
            errorMessage = dataHolder.errorMessageValidation.value,
            isHasError = dataHolder.isNameError.value,
        )

        TextInputWithTitle(
            dataHolder.email,
            title = stringResource(R.string.email),
            placeHolder = stringResource(R.string.enter_your_email),
            errorMessage = dataHolder.errorMessageValidation.value,
            isHasError = dataHolder.isEmailError.value,
        )
        TextInputWithTitle(
            dataHolder.phone,
            title = stringResource(R.string.phone),
            placeHolder = stringResource(R.string.enter_phone),
            errorMessage = dataHolder.errorMessageValidation.value,
            isHasError = dataHolder.isPhoneError.value,
        )
        TextSecureInputWithTitle(
            dataHolder.password,
            stringResource(R.string.password),
            dataHolder.isPasswordError.value,
            dataHolder.errorMessageValidation.value,
        )
        TextSecureInputWithTitle(
            dataHolder.confirmPassword,
            stringResource(R.string.confirm_password),
            dataHolder.isPasswordConfirm.value,
            dataHolder.errorMessageValidation.value,
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
                checked = dataHolder.isCheckBox.value,
                onCheckedChange = { updateClickPolicesAndTerm(!dataHolder.isCheckBox.value) },
                colors = CheckboxDefaults.colors(
                    checkedColor = CustomColor.primaryColor700
                ),
                modifier = Modifier.padding()
            )
            Text(
                stringResource(R.string.agree_with),
                fontFamily = General.satoshiFamily,
                fontWeight = FontWeight.Normal,
                color = if (dataHolder.isTermAndServicesError.value) CustomColor.alertColor_1_400 else CustomColor.neutralColor950,
                fontSize = (16 / fontScall).sp,
            )
            Text(
                stringResource(R.string.term_condition),
                fontFamily = General.satoshiFamily,
                fontWeight = FontWeight.Medium,
                color = if (dataHolder.isTermAndServicesError.value) CustomColor.alertColor_1_400 else CustomColor.primaryColor700,
                fontSize = (16 / fontScall).sp,
                modifier = Modifier
                    .padding(start = 3.dp),
                textDecoration = TextDecoration.Underline
            )
        }
        if (dataHolder.isTermAndServicesError.value)
            Text(
                dataHolder.errorMessageValidation.value,
                color = CustomColor.alertColor_1_400,
                fontFamily = General.satoshiFamily,
                fontWeight = FontWeight.Medium,
                fontSize = (14 / fontScall).sp,
                modifier = Modifier
                    .offset(x = (13).dp, y = (-12).dp)
            )

        CustomAuthBottom(
            isLoading = dataHolder.isLoading.value,
            validationFun = {
                validateInput()
            },
            buttonTitle = stringResource(R.string.signup),
            operation = { signupFun() }
        )
    }

}


@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun ExpandedSignupLayout(
    contentPadding: PaddingValues,
    dataHolder: SignupScreenDataHolder,
    scrollState: ScrollState,
    fontScall: Float,
    layoutDirection: LayoutDirection,
    validateInput: () -> Boolean,
    updateClickPolicesAndTerm: (state: Boolean) -> Unit,
    signupFun: () -> Unit
) {

    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp





    Row(
        modifier = Modifier
            .padding(
                start = 15.dp + contentPadding.calculateLeftPadding(layoutDirection),
                end = 15.dp + contentPadding.calculateRightPadding(layoutDirection),
                top = 5.dp+contentPadding.calculateTopPadding(),
                bottom = 5.dp+contentPadding.calculateBottomPadding()

            )
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
                .padding(
                    top = contentPadding.calculateTopPadding(),
                    bottom = contentPadding.calculateBottomPadding()
                )
                .padding(horizontal = 15.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        )
        {


            TextInputWithTitle(
                dataHolder.name,
                title = stringResource(R.string.name),
                placeHolder = stringResource(R.string.enter_your_name),
                errorMessage = dataHolder.errorMessageValidation.value,
                isHasError = dataHolder.isNameError.value,
            )

            TextInputWithTitle(
                dataHolder.email,
                title = stringResource(R.string.email),
                placeHolder = stringResource(R.string.enter_your_email),
                errorMessage = dataHolder.errorMessageValidation.value,
                isHasError = dataHolder.isEmailError.value,
            )
            TextInputWithTitle(
                dataHolder.phone,
                title = stringResource(R.string.phone),
                placeHolder = stringResource(R.string.enter_phone),
                errorMessage = dataHolder.errorMessageValidation.value,
                isHasError = dataHolder.isPhoneError.value,
            )
            TextSecureInputWithTitle(
                dataHolder.password,
                stringResource(R.string.password),
                dataHolder.isPasswordError.value,
                dataHolder.errorMessageValidation.value,
            )
            TextSecureInputWithTitle(
                dataHolder.confirmPassword,
                stringResource(R.string.confirm_password),
                dataHolder.isPasswordConfirm.value,
                dataHolder.errorMessageValidation.value,
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
                    checked = dataHolder.isCheckBox.value,
                    onCheckedChange = { updateClickPolicesAndTerm(!dataHolder.isCheckBox.value) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = CustomColor.primaryColor700
                    ),
                    modifier = Modifier.padding()
                )
                Text(
                    stringResource(R.string.agree_with),
                    fontFamily = General.satoshiFamily,
                    fontWeight = FontWeight.Normal,
                    color = if (dataHolder.isTermAndServicesError.value) CustomColor.alertColor_1_400 else CustomColor.neutralColor950,
                    fontSize = (16 / fontScall).sp,
                )
                Text(
                    stringResource(R.string.term_condition),
                    fontFamily = General.satoshiFamily,
                    fontWeight = FontWeight.Medium,
                    color = if (dataHolder.isTermAndServicesError.value) CustomColor.alertColor_1_400 else CustomColor.primaryColor700,
                    fontSize = (16 / fontScall).sp,
                    modifier = Modifier
                        .padding(start = 3.dp),
                    textDecoration = TextDecoration.Underline
                )
            }
            if (dataHolder.isTermAndServicesError.value)
                Text(
                    dataHolder.errorMessageValidation.value,
                    color = CustomColor.alertColor_1_400,
                    fontFamily = General.satoshiFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = (14 / fontScall).sp,
                    modifier = Modifier
                        .offset(x = (13).dp, y = (-12).dp)
                )

            CustomAuthBottom(
                isLoading = dataHolder.isLoading.value,
                validationFun = { validateInput() },
                buttonTitle = stringResource(R.string.signup),
                operation = { signupFun() }
            )
        }

    }
}