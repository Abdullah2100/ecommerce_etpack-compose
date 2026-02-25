package com.example.eccomerce_app.ui.view.home

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.retain.retain
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.example.eccomerce_app.util.General
import com.example.eccomerce_app.ui.Screens
import com.example.eccomerce_app.ui.component.Sizer
import com.example.e_commercompose.ui.theme.CustomColor
import com.example.eccomerce_app.viewModel.ProductViewModel
import com.example.eccomerce_app.viewModel.CategoryViewModel
import com.example.e_commercompose.R
import com.example.eccomerce_app.ui.component.SharedAppBar
import com.example.eccomerce_app.viewModel.CartViewModel
import com.example.eccomerce_app.viewModel.BannerViewModel
import com.example.eccomerce_app.viewModel.CurrencyViewModel
import com.example.eccomerce_app.viewModel.StoreViewModel
import com.example.eccomerce_app.viewModel.SubCategoryViewModel
import com.example.eccomerce_app.viewModel.UserViewModel
import com.example.eccomerce_app.viewModel.VariantViewModel
import com.example.eccomerce_app.ui.view.account.store.ProductDetail
import kotlinx.coroutines.launch
import java.util.UUID




@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun CategoryScreen(
    nav: NavHostController,
    categoryViewModel: CategoryViewModel,
    productViewModel: ProductViewModel,
    isShowArrowBackNavIcon: Boolean = true,
) {
    val context = LocalContext.current


    val category = categoryViewModel.categories.collectAsStateWithLifecycle()
    val coroutine = rememberCoroutineScope()

    fun getProductCategoryAndNavigate(id: UUID) {
        coroutine.launch {
            productViewModel.getProductsByCategoryID(
                1,
                id,
            )
            nav.navigate(
                Screens.ProductCategory(
                    id.toString()
                )
            )
        }
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        topBar = {
            SharedAppBar(
                title = stringResource(R.string.category),
                nav = nav,
                isShowArrowBackIcon = isShowArrowBackNavIcon)

        }

    )
    { scaffoldState ->
        scaffoldState.calculateTopPadding()
        scaffoldState.calculateBottomPadding()
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            item {


                FlowRow(
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .fillMaxWidth(),

                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    repeat(category.value?.size ?: 0)
                    { index ->
                        Column(
                            modifier = Modifier
                                .background(Color.White)
                                .width(80.dp)
                                .clickable {
                                    getProductCategoryAndNavigate(category.value!![index].id)
                                },
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            SubcomposeAsyncImage(
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .height(80.dp)
                                    .width(80.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                model = General.handlingImageForCoil(
                                    category.value?.get(index)?.image,
                                    context
                                ),
                                contentDescription = "",
                                loading = {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            color = Color.Black,
                                            modifier = Modifier.size(53.dp)
                                        )
                                    }
                                },
                            )
                            Sizer(width = 10)
                            Text(
                                category.value?.get(index)?.name ?: "",
                                fontFamily = General.satoshiFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = (16).sp,
                                color = CustomColor.neutralColor950,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            }
            item {
                Box(modifier = Modifier.height(190.dp))
            }
        }
    }

}

