package com.example.eccomerce_app.ui.view.OnBoarding

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.eccomerce_app.util.General
import com.example.e_commercompose.R
import com.example.eccomerce_app.ui.Screens
import com.example.e_commercompose.ui.theme.CustomColor
import com.example.eccomerce_app.ui.component.Sizer
import com.example.eccomerce_app.viewModel.UserViewModel


@SuppressLint("ContextCastToActivity")
@Composable
fun OnBoardingScreen(
    nav: NavController, userViewModel: UserViewModel
) {
    val fontScall = LocalDensity.current.fontScale
    val layoutDirection = LocalLayoutDirection.current

    val scrollState = rememberScrollState()


    fun navToHome() {
        userViewModel.setIsPassOnBoardingScreen()

        nav.navigate(Screens.AuthGraph) {
            popUpTo(nav.graph.id) {
                inclusive = true
            }
        }

    }
    Scaffold(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize(),
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(
                    start = 15.dp + contentPadding.calculateLeftPadding(layoutDirection),
                    end = 15.dp + contentPadding.calculateRightPadding(layoutDirection),
                    top = 5.dp + contentPadding.calculateTopPadding(),
                    bottom = 5.dp + contentPadding.calculateBottomPadding()
                )
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier.weight(1f)
            )

            Image(
                imageVector = ImageVector.vectorResource(R.drawable.onboarding_log),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.padding(top = 20.dp)

            )

            Text(
                stringResource(R.string.welcome_to_shopzen),
                fontWeight = FontWeight.Bold,
                fontFamily = General.satoshiFamily,
                color = CustomColor.neutralColor950,
                fontSize = ((32 / fontScall).sp)
            )

            Text(
                stringResource(R.string.your_one_stop_destination_for_hassle_free_online_shopping),
                color = CustomColor.neutralColor800,
                fontSize = (18 / fontScall).sp,
                textAlign = TextAlign.Center,
                fontFamily = General.satoshiFamily,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 5.dp)
            )

            Button(
                modifier = Modifier
                    .padding(
                        top = 10.dp,
                    )
                    .height(50.dp)
                    .fillMaxWidth(),
                onClick = { navToHome() },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CustomColor.primaryColor700
                ),

                ) {

                Text(
                    stringResource(R.string.get_started),
                    fontFamily = General.satoshiFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = (16 / fontScall).sp
                )
            }
        }

    }
}