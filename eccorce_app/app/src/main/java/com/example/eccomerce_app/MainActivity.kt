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


       lifecycleScope.launch{
           currentLocal.collect { value->
               whenLanguageUpdateDo(value?:"ar",this@MainActivity)
           }
       }





        installSplashScreen().apply {
            setKeepOnScreenCondition {
                keepSplash
            }
        }

        enableEdgeToEdge()
        setContent {


            val nav = rememberNavController()
            val currentScreen = authViewModel.currentScreen.collectAsStateWithLifecycle()
            val currentLocale = currentLocal.collectAsStateWithLifecycle()
            val navBackStackEntry = nav.currentBackStackEntryAsState()
            val isHasBottonSheet =   navBackStackEntry.value?.destination?.hasRoute(Screens.Home::class) == true ||
                    navBackStackEntry.value?.destination?.hasRoute(Screens.Cart::class) == true ||
                    navBackStackEntry.value?.destination?.hasRoute(Screens.Order::class) == true ||
                    navBackStackEntry.value?.destination?.hasRoute(Screens.Account::class) == true||
                    navBackStackEntry.value?.destination?.hasRoute(Screens.Home::class) == true


            val buttonNavItemHolder = listOf(
                ButtonNavItem(
                    name = R.string.home,
                    imageId = Icons.Outlined.Home,
                    0
                ),
                ButtonNavItem(
                    name = R.string.order,
                    imageId = ImageVector.vectorResource(R.drawable.order),
                    1
                ),
                ButtonNavItem(
                    name = R.string.cart,
                    imageId = Icons.Outlined.ShoppingCart,
                    2
                ),
                ButtonNavItem(
                    name = R.string.account,
                    imageId = Icons.Outlined.Person,
                    3
                ),
            )



            val pages = listOf(
                Screens.Home,
                Screens.Order,
                Screens.Cart,
                Screens.Account,
            )

            val updateDirection = remember {
                derivedStateOf {
                    if (currentLocale.value == "ar") {
                        LayoutDirection.Rtl

                    } else {
                        LayoutDirection.Ltr
                    }
                }
            }




            if (currentScreen.value != null) {
                keepSplash = false
            }

            LaunchedEffect(navBackStackEntry.value?.destination) {
                Log.d("currentNavName","${navBackStackEntry.value?.destination?.route?:"no route"} ${isHasBottonSheet.toString()}")
            }

            if (currentScreen.value != null)

                CompositionLocalProvider(
                    LocalLayoutDirection provides updateDirection.value,

                ) {
                    NavigationSuiteScaffold(
                        navigationSuiteItems = {
                            if (isHasBottonSheet) {
                                buttonNavItemHolder.fastForEachIndexed { index,value->
                                    val isSelectedItem =
                                        navBackStackEntry.value?.destination?.hasRoute(
                                            pages[index]::class
                                        ) == true
                                    item(
                                        selected = isSelectedItem,
                                        onClick = {nav.navigate(pages[index])} ,
                                        icon = {Icon(value.imageId,"")},
                                        label = {  Text(
                                            text = stringResource(value.name),
                                            fontFamily = General.satoshiFamily,
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 12.sp,
                                            textAlign = TextAlign.Center,
                                            color = if (isSelectedItem) CustomColor.primaryColor950 else Color.Gray
                                        )},

                                    )
                                }

                            }

                    }
                    ,
                        containerColor = Color.White,
                        layoutType =
                            if(!isHasBottonSheet) NavigationSuiteType.None
                            else
                                NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfo()),
                        )
                {

                    NavController(nav, currentScreen = currentScreen.value ?: 1)
                }
                }
        }
    }


    override fun onDestroy() {
//        connection?.stop()
        super.onDestroy()
    }
}
