package com.example.eccomerce_app.model

import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.input.TextFieldValue

data class LoginScreenDataHolder(
val userNameOrEmail: MutableState<TextFieldValue>,
val password: MutableState<TextFieldValue>,
val errorMessageValidation: MutableState<String>,
val isSendingData: MutableState<Boolean>,
val isEmailError: MutableState<Boolean?>
)
