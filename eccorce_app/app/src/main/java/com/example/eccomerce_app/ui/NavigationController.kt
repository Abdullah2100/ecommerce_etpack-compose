package com.example.eccomerce_app.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel
import androidx.navigation.NavHostController
import com.example.eccomerce_app.viewModel.AuthViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.eccomerce_app.viewModel.CartViewModel
import com.example.eccomerce_app.ui.view.OnBoarding.OnBoardingScreen
import com.example.eccomerce_app.ui.view.Auth.LoginScreen
import com.example.eccomerce_app.ui.view.Auth.SignUpPage
import com.example.eccomerce_app.ui.view.account.AccountPage
import com.example.eccomerce_app.ui.view.checkout.CheckoutScreen
import com.example.eccomerce_app.ui.view.home.CartScreen
import com.example.eccomerce_app.ui.view.home.HomePage
import com.example.eccomerce_app.ui.view.home.OrderScreen
import com.example.eccomerce_app.ui.view.ReseatPassword.GenerateOtpScreen
import com.example.eccomerce_app.ui.view.ReseatPassword.OtpVerificationScreen
import com.example.eccomerce_app.ui.view.ReseatPassword.ReseatPasswordScreen
import com.example.eccomerce_app.ui.view.address.AddressHomeScreen
import com.example.eccomerce_app.viewModel.ProductViewModel
import com.example.eccomerce_app.viewModel.StoreViewModel
import com.example.eccomerce_app.viewModel.SubCategoryViewModel
import com.example.eccomerce_app.viewModel.VariantViewModel
import com.example.eccomerce_app.viewModel.BannerViewModel
import com.example.eccomerce_app.viewModel.CategoryViewModel
import com.example.eccomerce_app.viewModel.CurrencyViewModel
import com.example.eccomerce_app.viewModel.DeliveryViewModel
import com.example.eccomerce_app.viewModel.GeneralSettingViewModel
import com.example.eccomerce_app.viewModel.HomeViewModel
import com.example.eccomerce_app.viewModel.MapViewModel
import com.example.eccomerce_app.viewModel.OrderItemsViewModel
import com.example.eccomerce_app.viewModel.OrderViewModel
import com.example.eccomerce_app.viewModel.PaymentTypeViewModel
import com.example.eccomerce_app.viewModel.PaymentViewModel
import com.example.eccomerce_app.viewModel.UserViewModel


@Composable
fun NavController(
    nav: NavHostController,
    authViewModel: AuthViewModel = koinViewModel(),
    cartViewModel: CartViewModel = koinViewModel(),
    bannerViewModel: BannerViewModel = koinViewModel(),
    categoryViewModel: CategoryViewModel = koinViewModel(),
    subCategoryViewModel: SubCategoryViewModel = koinViewModel(),
    variantViewModel: VariantViewModel = koinViewModel(),
    storeViewModel: StoreViewModel = koinViewModel(),
    productViewModel: ProductViewModel = koinViewModel(),
    userViewModel: UserViewModel = koinViewModel(),
    generalSettingViewModel: GeneralSettingViewModel = koinViewModel(),
    orderViewModel: OrderViewModel = koinViewModel(),
    orderItemViewModel: OrderItemsViewModel = koinViewModel(),
    mapViewModel: MapViewModel = koinViewModel(),
    deliveryViewModel: DeliveryViewModel = koinViewModel(),
    homeViewModel: HomeViewModel = koinViewModel(),
    currencyViewModel: CurrencyViewModel = koinViewModel(),
    paymentViewModel: PaymentViewModel = koinViewModel(),
    paymentTypeViewModel: PaymentTypeViewModel = koinViewModel(),
    currentScreen: Int,
) {

    NavHost(
        startDestination = when (currentScreen) {
            1 -> Screens.OnBoarding
            2 -> Screens.AuthGraph
            3 -> Screens.LocationHome
            else -> Screens.HomeGraph
        }, navController = nav
    ) {


        composable<Screens.OnBoarding>(
            enterTransition = {
                return@composable fadeIn(tween(2000))
            },
            exitTransition = {
                return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(700)
                )
            },

            ) {
            OnBoardingScreen(nav = nav, userViewModel = userViewModel)
        }





        navigation<Screens.ReseatPasswordGraph>(
            startDestination = Screens.GenerateOtp
        )
        {

            composable<Screens.GenerateOtp>(enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(750)
                )
            }, exitTransition = {
                return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(750)
                )
            }) {
                GenerateOtpScreen(
                    nav = nav, authViewModel
                )
            }
            composable<Screens.OtpVerification>(enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(750)
                )
            }, exitTransition = {
                return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(750)
                )
            }) { result ->
                val data = result.toRoute<Screens.OtpVerification>()
                OtpVerificationScreen(
                    nav = nav, authViewModel, email = data.email
                )
            }

            composable<Screens.ReseatPassword>(enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(750)
                )
            }, exitTransition = {
                return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(750)
                )
            }) { result ->
                val data = result.toRoute<Screens.ReseatPassword>()
                ReseatPasswordScreen(
                    nav = nav, authViewModel, email = data.email, otp = data.otp
                )
            }

        }

        navigation<Screens.AuthGraph>(
            startDestination = Screens.Login
        )
        {

            composable<Screens.Login>(
                enterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start, tween(750)
                    )
                },

                popEnterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start, tween(750)
                    )
                }, exitTransition = {
                    return@composable slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.End, tween(750)
                    )
                }) {
                LoginScreen(
                    nav = nav, authKoin = authViewModel
                )
            }

            composable<Screens.Signup>(
                enterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.End, tween(750)
                    )
                },

                popEnterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start, tween(750)
                    )
                },
            ) {
                SignUpPage(
                    nav = nav, authKoin = authViewModel
                )
            }
        }

        navigation<Screens.HomeGraph>(
            startDestination = Screens.Home
        )
        {

            composable<Screens.Home>() {
                HomePage(
                    nav = nav,
                    bannerViewModel = bannerViewModel,
                    categoryViewModel = categoryViewModel,
                    variantViewModel = variantViewModel,
                    productViewModel = productViewModel,
                    userViewModel = userViewModel,
                    generalSettingViewModel = generalSettingViewModel,
                    orderViewModel = orderViewModel,
                    currencyViewModel = currencyViewModel,
                    homeViewModel = homeViewModel,
                    paymentTypeViewModel = paymentTypeViewModel,
                    cartViewModel = cartViewModel,
                    storeViewModel = storeViewModel,
                    subCategoryViewModel = subCategoryViewModel,
                )
            }


            composable<Screens.Account>() {
                AccountPage(
                    nav = nav,
                    userViewModel = userViewModel,
                    orderItemsViewModel = orderItemViewModel,
                    authViewModel = authViewModel,
                    productViewModel = productViewModel,
                    currencyViewModel = currencyViewModel,
                    storeViewModel = storeViewModel,
                    bannerViewModel = bannerViewModel,
                    categoryViewModel = categoryViewModel,
                    subCategoryViewModel = subCategoryViewModel,
                    mapViewModel = mapViewModel,
                    cartViewModel = cartViewModel,
                    variantViewModel = variantViewModel,
                    deliveryViewModel = deliveryViewModel
                )
            }


        }


        composable<Screens.LocationHome>(
            enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(750)
                )
            },

            popEnterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(750)
                )
            }, exitTransition = {
                return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(750)
                )
            }) {
            AddressHomeScreen(
                nav = nav,
                userViewModel = userViewModel,
                orderViewModel = orderViewModel,
                variantViewModel = variantViewModel,
                generalSettingViewModel = generalSettingViewModel,
                bannerViewModel = bannerViewModel,
                categoryViewModel = categoryViewModel,
                productViewModel = productViewModel,
                cartViewModel = cartViewModel,
                storeViewModel = storeViewModel,
                mapViewModel = mapViewModel
            )

        }

        composable<Screens.Cart>() {
            CartScreen(
                nav = nav,
                cartViewModel = cartViewModel,
                variantViewModel = variantViewModel,
                userViewModel = userViewModel,
                storeViewModel = storeViewModel,
                currencyViewModel = currencyViewModel,
                paymentTypeViewModel = paymentTypeViewModel,
                paymentViewModel = paymentViewModel,
                orderViewModel = orderViewModel,
                generalSettingViewModel = generalSettingViewModel
            )
        }



        composable<Screens.Order>()
        {
            OrderScreen(orderViewModel = orderViewModel)

        }


    }

}

