package com.example.eccomerce_app.model

import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.input.TextFieldValue

data class SignupScreenDataHolder(
var name: MutableState<TextFieldValue>,
var email: MutableState<TextFieldValue>,
var phone: MutableState<TextFieldValue>,
var password: MutableState<TextFieldValue>,
var confirmPassword: MutableState<TextFieldValue>,
var isLoading: MutableState<Boolean>,
var isCheckBox: MutableState<Boolean>,
var isNameError: MutableState<Boolean>,
var isPhoneError: MutableState<Boolean>,
var isPasswordError: MutableState<Boolean>,
var isPasswordConfirm: MutableState<Boolean>,
var isTermAndServicesError: MutableState<Boolean>,

var errorMessageValidation: MutableState<String>,
var isEmailError: MutableState<Boolean>,
)
