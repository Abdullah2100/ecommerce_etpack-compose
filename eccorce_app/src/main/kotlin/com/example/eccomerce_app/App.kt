package com.example.eccomerce_app

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.e_commercompose.R
import com.example.eccomerce_app.model.ButtonNavItem
import com.example.eccomerce_app.ui.NavController
import com.example.eccomerce_app.ui.Screens
import com.example.e_commercompose.ui.theme.CustomColor
import com.example.eccomerce_app.util.General
import com.example.eccomerce_app.viewModel.AuthViewModel
import org.koin.compose.koinInject
import androidx.compose.ui.res.vectorResource
@Composable
fun App() {
    val authViewModel: AuthViewModel = koinInject()
    val nav = rememberNavController()
    val currentScreen by authViewModel.currentScreen.collectAsStateWithLifecycle()
    val currentLocale by General.currentLocal.collectAsStateWithLifecycle()
    val navBackStackEntry by nav.currentBackStackEntryAsState()

    val isHasBottonSheet = navBackStackEntry?.destination?.hasRoute(Screens.Home::class) == true ||
            navBackStackEntry?.destination?.hasRoute(Screens.Cart::class) == true ||
            navBackStackEntry?.destination?.hasRoute(Screens.Order::class) == true ||
            navBackStackEntry?.destination?.hasRoute(Screens.Account::class) == true ||
            navBackStackEntry?.destination?.hasRoute(Screens.Home::class) == true

    val buttonNavItemHolder = listOf(
        ButtonNavItem(
            name = R.string.home,
            imageId = Icons.Outlined.Home,
            index = 0
        ),

        ButtonNavItem(
            name = R.string.order, // Changed from order to my_order if order doesn't exist? Assuming 'order' exists.
            imageId = ImageVector.vectorResource(R.drawable.order),
            index = 1
        ),

        ButtonNavItem(
            name = R.string.cart,
            imageId = Icons.Outlined.ShoppingCart,
            index = 2
        ),
        ButtonNavItem(
            name = R.string.account,
            imageId = Icons.Outlined.Person,
            index = 3
        ),
    )

    val pages = listOf(
        Screens.Home,
        Screens.Order,
        Screens.Cart,
        Screens.Account,
    )

    val updateDirection = remember(currentLocale) {
        if (currentLocale == "ar") {
            LayoutDirection.Rtl
        } else {
            LayoutDirection.Ltr
        }
    }

    CompositionLocalProvider(
        LocalLayoutDirection provides updateDirection,
    ) {
        NavigationSuiteScaffold(
            navigationSuiteItems = {
                if (isHasBottonSheet) {
                    buttonNavItemHolder.fastForEachIndexed { index, value ->
                        val isSelectedItem =
                            navBackStackEntry?.destination?.hasRoute(
                                pages[index]::class
                            ) == true
                        item(
                            selected = isSelectedItem,
                            onClick = { nav.navigate(pages[index]) },
                            icon = { Icon(value.imageId, "") },
                            label = {
                                Text(
                                    text = stringResource(value.name),
                                    // fontFamily = General.satoshiFamily, // Commented out until fonts are fixed
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center,
                                    color = if (isSelectedItem) CustomColor.primaryColor950 else Color.Gray
                                )
                            },
                        )
                    }
                }
            },
            containerColor = Color.White,
            layoutType = if (!isHasBottonSheet) NavigationSuiteType.None
            else NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfo()),
        ) {
            NavController(nav, currentScreen = currentScreen ?: 1)
        }
    }
}
