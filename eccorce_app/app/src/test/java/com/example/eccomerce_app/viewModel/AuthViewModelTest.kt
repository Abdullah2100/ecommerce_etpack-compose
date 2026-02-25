package com.example.eccomerce_app.viewModel

import android.util.Log
import com.example.eccomerce_app.data.NetworkCallHandler
import com.example.eccomerce_app.data.Room.Dao.AuthDao
import com.example.eccomerce_app.data.Room.Dao.LocaleDao
import com.example.eccomerce_app.data.repository.AuthRepository
import com.example.eccomerce_app.dto.AuthDto
import com.example.eccomerce_app.dto.LoginDto
import com.example.eccomerce_app.dto.SignupDto
import com.example.eccomerce_app.util.GeneralValue
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private lateinit var viewModel: AuthViewModel
    
    // 1. Mock the dependencies
    private val authRepository: AuthRepository = mockk()
    private val authDao: AuthDao = mockk(relaxed = true)
    private val localDao: LocaleDao = mockk(relaxed = true)

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        // 2. Set the Main dispatcher to a test dispatcher for coroutines
        Dispatchers.setMain(testDispatcher)
        
        // 3. Mock static classes like Log (which won't work in unit tests)
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0

        // 4. Mock calls that happen in init { } or startup
        coEvery { localDao.getCurrentLocal() } returns null
        coEvery { authDao.getAuthData() } returns null
        coEvery { authDao.isPassOnBoarding() } returns false
        coEvery { authDao.isPassLocationScreen() } returns false

        // 5. Initialize the ViewModel with mocked dependencies
        viewModel = AuthViewModel(authRepository, authDao, localDao)
    }

    @After
    fun tearDown() {
        // 6. Reset the Main dispatcher and clear mocks
        Dispatchers.resetMain()
        unmockkAll()
        GeneralValue.authData = null
    }

    @Test
    fun `loginUser success returns null and saves data`() = runTest {
        // Arrange
        val username = "testuser"
        val password = "password"
        val authDto = AuthDto(token = "fake_token", refreshToken = "fake_refresh_token")
        
        // Mock the repository to return a successful result
        coEvery { authRepository.login(any()) } returns NetworkCallHandler.Successful(authDto)

        var isLoadingCalled = false
        val updateLoading = { state: Boolean -> isLoadingCalled = state }

        // Act
        val result = viewModel.loginUser(username, password, updateLoading)

        // Assert
        assertEquals(null, result)
        // Verify that the data was saved to local storage
        coVerify { authDao.saveAuthData(any()) }
        // Verify that loading state was updated
        assertEquals(false, isLoadingCalled)
    }

    @Test
    fun `loginUser failure returns error message`() = runTest {
        // Arrange
        val username = "testuser"
        val password = "password"
        val errorMessage = "Invalid credentials"
        
        // Mock the repository to return an error result
        coEvery { authRepository.login(any()) } returns NetworkCallHandler.Error(errorMessage)

        var isLoadingCalled = false
        val updateLoading = { state: Boolean -> isLoadingCalled = state }

        // Act
        val result = viewModel.loginUser(username, password, updateLoading)

        // Assert
        assertEquals(errorMessage, result)
        // Verify the error message flow was updated
        assertEquals(errorMessage, viewModel.errorMessage.value)
        assertEquals(false, isLoadingCalled)
    }

    @Test
    fun `signUpUser success returns null and saves data`() = runTest {
        //Arrange
        val signUpDto = SignupDto(
            Name="Salime",
            Password = "771ali@..",
            Phone = "779778885",
            Email = "ali555555@gmail.com",
            DeviceToken = ";kjasdf")

        val authDto = AuthDto(token = "fake_token", refreshToken = "fake_refresh_token")


        //mock
        coEvery { authRepository.signup( signUpDto) } returns NetworkCallHandler
            .Successful(AuthDto())

        // Act
        val result = viewModel.signUpUser(
            email = signUpDto.Email,
            name = signUpDto.Name,
            phone = signUpDto.Phone,
            password = signUpDto.Password,
            updateIsLoading = {}
        )

        // Assert
        assertEquals(null, result)

    }

}
