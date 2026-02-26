package com.example.eccomerce_app.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eccomerce_app.util.General
import com.example.core.database.Dao.AuthDao
import com.example.core.database.Model.AuthModelEntity
import com.example.core.network.dto.LoginDto
import com.example.core.network.dto.AuthDto
import com.example.core.network.dto.SignupDto
import com.example.core.database.Dao.LocaleDao
import com.example.core.network.NetworkCallHandler
import com.example.core.network.Secrets
import com.example.core.network.repository.AuthRepository
import com.example.eccomerce_app.util.General.currentLocal
import com.example.eccomerce_app.util.GeneralValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class AuthViewModel(
    val authRepository:
    AuthRepository,
    val authDao: AuthDao,
    val localDao: LocaleDao
) : ViewModel() {

//     val isLoading: StateFlow<Boolean> field = MutableStateFlow(false)

    val errorMessage = MutableStateFlow<String?>(null)


    private val _currentScreen = MutableStateFlow<Int?>(null)
    val currentScreen = _currentScreen.asStateFlow()

    suspend fun clearErrorMessage() {
        errorMessage.emit(null)
    }



    init {
        getCurrentLocalization()
        getStartedScreen()
    }

    fun getCurrentLocalization() {
        viewModelScope.launch {
            val dbLocale = localDao.getCurrentLocal()
            Log.d("CurrentLocalization", dbLocale?.name ?: "no data")

            if (dbLocale == null) {
                return@launch
            }
            currentLocal.emit(dbLocale.name);
        }
    }

    fun getStartedScreen() {
        CoroutineScope(Dispatchers.IO).launch {
            val authData = authDao.getAuthData()
            val isPassOnBoard = authDao.isPassOnBoarding()
            val isLocation = authDao.isPassLocationScreen()
            Log.d("AuthDataIs", isPassOnBoard.toString())
            if (authData != null)
                GeneralValue.authData = authData
            when (isPassOnBoard) {
                false -> {
                    _currentScreen.emit(1)
                }

                else -> {
                    when (authData == null) {
                        true -> {
                            _currentScreen.emit(2)
                        }

                        else -> {
                            when (!isLocation) {
                                true -> {
                                    _currentScreen.emit(3)
                                }

                                else -> {
                                    _currentScreen.emit(4)
                                }
                            }
                        }
                    }
                }
            }

        }
    }


    suspend fun generateTokenNotification(): String? {
        return try {
//            val token = FirebaseMessaging.getInstance().token.await()
//            token
            "fv6pNFrXSsC7o29xq991br:APA91bHiUFcyvxKKxcqWoPZzoIaeWEs6_uN36YI0II5HHpN3HP-dUQap9UbnPiyBB8Fc5xX6GiCYbDQ7HxuBlXZkAE2P0T82-DRQ160EiKCJ9tlPgfgQxa4"

        } catch (e: Exception) {
            null
        }
    }

    suspend fun signUpUser(
        email: String,
        name: String,
        phone: String,
        password: String,
        updateIsLoading: (state: Boolean) -> Unit
    ): String? {

        val token = generateTokenNotification()

        if (token == null) {
            updateIsLoading.invoke(false)
            return "Could not Get Device Info"
        }

        val result = authRepository.signup(
            SignupDto(
                Name = name,
                Password = password,
                Phone = phone,
                Email = email,
                DeviceToken = token
            )
        )

        return when (result) {
            is NetworkCallHandler.Successful<*> -> {
                updateIsLoading.invoke(false)

                val authData = result.data as AuthDto

                val authDataHolder = AuthModelEntity(
                    id = 0,
                    token = authData.token,
                    refreshToken = authData.refreshToken
                )
                authDao.saveAuthData(
                    authDataHolder
                )

                GeneralValue.authData = authDataHolder
                null;

            }

            is NetworkCallHandler.Error -> {
                updateIsLoading.invoke(false)
                val errorResult = (result.data.toString())
                if (errorResult.contains(Secrets.getUrl())) {
                    errorResult.replace(Secrets.getUrl(), " Server ")
                }
                errorResult
            }

        }

    }

    suspend fun loginUser(
        username: String,
        password: String,
        updateStateLoading: (state: Boolean) -> Unit
    ): String? {
        updateStateLoading.invoke(true)
        val token = generateTokenNotification()
        if (token == null) {
            updateStateLoading.invoke(false)
            return "Could not Get Device Info"
        }

        val result = authRepository.login(
            LoginDto(
                Username = username,
                Password = password,
                DeviceToken = token
            )
        )

        return when (result) {
            is NetworkCallHandler.Successful<*> -> {
                updateStateLoading.invoke(false)
                val authData = result.data as AuthDto
                val authDataHolder = AuthModelEntity(
                    id = 0,
                    token = authData.token,
                    refreshToken = authData.refreshToken
                )
                authDao.saveAuthData(
                    authDataHolder
                )
                GeneralValue.authData = authDataHolder
                null
            }

            is NetworkCallHandler.Error -> {
                updateStateLoading.invoke(false)
                val errorResult = (result.data?.replace("\"", ""))

                errorMessage.emit(errorResult)
                errorResult
            }


        }
    }


    suspend fun getOtp(
        email: String,
        updateIsLoading: (state: Boolean) -> Unit
    ): String? {

        updateIsLoading.invoke(true)
        when (val result = authRepository.getOtp(email)) {
            is NetworkCallHandler.Successful<*> -> {
                updateIsLoading.invoke(false)
                return null
            }

            is NetworkCallHandler.Error -> {
                updateIsLoading.invoke(false)
                val errorMessage = (result.data.toString())
                if (errorMessage.contains(Secrets.getUrl())) {
                    errorMessage.replace(Secrets.getUrl(), " Server ")
                }
                return errorMessage
            }

        }
    }

    suspend fun otpVerifying(
        email: String,
        otp: String,
        updateIsLoading: (state: Boolean) -> Unit
    ): String? {

        updateIsLoading.invoke(true)

        when (val result = authRepository.verifyingOtp(email, otp)) {
            is NetworkCallHandler.Successful<*> -> {
                updateIsLoading.invoke(false)
                return null
            }

            is NetworkCallHandler.Error -> {
                updateIsLoading.invoke(false)

                val errorMessage = (result.data.toString())
                if (errorMessage.contains(Secrets.getUrl())) {
                    errorMessage.replace(Secrets.getUrl(), " Server ")
                }
                return errorMessage
            }

        }
    }


    suspend fun reseatPassword(
        email: String,
        otp: String,
        newPassword: String

    ): String? {
        when (val result = authRepository.resetPassword(email, otp, newPassword)) {
            is NetworkCallHandler.Successful<*> -> {
                val authData = result.data as AuthDto
                val authDataHolder = AuthModelEntity(
                    id = 0,
                    token = authData.token,
                    refreshToken = authData.refreshToken
                )
                authDao.saveAuthData(
                    authDataHolder
                )
                GeneralValue.authData = authDataHolder

                return null
            }

            is NetworkCallHandler.Error -> {

                val errorMessage = (result.data.toString())
                if (errorMessage.contains(Secrets.getUrl())) {
                    errorMessage.replace(Secrets.getUrl(), " Server ")
                }
                return errorMessage
            }

        }
    }

    fun logout() {
        CoroutineScope(Dispatchers.IO).launch {
            authDao.nukeAuthTable()
            authDao.nukeIsPassAddressTable()
        }
    }
}
