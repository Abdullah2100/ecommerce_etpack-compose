package com.example.eccomerce_app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.e_commercompose.R
import com.example.e_commercompose.model.ButtonNavItem
import com.example.e_commercompose.ui.theme.CustomColor
import com.example.eccomerce_app.ui.NavController
import com.example.eccomerce_app.ui.Screens
import com.example.eccomerce_app.util.General
import com.example.eccomerce_app.util.General.currentLocal
import com.example.eccomerce_app.util.General.whenLanguageUpdateDo
import com.example.eccomerce_app.viewModel.AuthViewModel
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform.getKoin

class MainActivity : ComponentActivity() {
    var keepSplash = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authViewModel: AuthViewModel = getKoin().get()


        lifecycleScope.launch {
            currentLocal.collect { value ->
                General.whenLanguageUpdateDo(value ?: "ar", this@MainActivity)
            }
        }





        installSplashScreen().apply {
            setKeepOnScreenCondition {
                keepSplash
            }
        }

        enableEdgeToEdge()
        setContent {


            val currentScreen = authViewModel.currentScreen.collectAsStateWithLifecycle()
            if (currentScreen.value != null) {
                keepSplash = false
            }

            if (currentScreen.value!= null)
                App()
        }
    }


    override fun onDestroy() {
//        connection?.stop()
        super.onDestroy()
    }
}