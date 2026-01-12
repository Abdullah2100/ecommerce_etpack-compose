package com.example.eccomerce_app.ui.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.e_commercompose.R
import com.example.e_commercompose.model.ProductVariant
import com.example.e_commercompose.ui.component.Sizer
import com.example.e_commercompose.ui.theme.CustomColor
import com.example.eccomerce_app.util.General
import java.util.UUID


@Composable
fun ProductVariantComponent(
    productVariants: List<List<ProductVariant>>,
    index: Int,
    variantName: String,
    selectedProductVariant: List<ProductVariant>,
    selectProductVariant: (productVariant: ProductVariant) -> Unit
) {

    Column {
        Text(
            text = stringResource(R.string.select),
            color = CustomColor.neutralColor950,
            fontFamily = General.satoshiFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 15.dp)
        )
        Sizer(10)
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = variantName,
                color = CustomColor.neutralColor950,
                fontFamily = General.satoshiFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 15.dp)
            )
            Sizer(width = 10)
            FlowRow(
                modifier = Modifier
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            )
            {
                repeat(productVariants[index].size) { pvIndex ->

                    val productVariantHolder = ProductVariant(
                        id = productVariants[index][pvIndex].id,
                        name = productVariants[index][pvIndex].name,
                        percentage = productVariants[index][pvIndex].percentage,
                        variantId = productVariants[index][pvIndex].variantId
                    )
                    when (variantName.lowercase() == "color") {
                        true -> {
                            val colorValue = General.convertColorToInt(productVariants[index][pvIndex].name)

                            if (colorValue != null)
                                Box(
                                    modifier = Modifier
                                        .height(24.dp)
                                        .width(24.dp)
                                        .border(
                                            width = if (selectedProductVariant.contains(
                                                    productVariantHolder
                                                )
                                            )
                                                1.dp else 0.dp,
                                            color = if (selectedProductVariant.contains(
                                                    productVariantHolder
                                                )
                                            )
                                                CustomColor.primaryColor700
                                            else Color.White,
                                            shape = RoundedCornerShape(20.dp)
                                        )
                                        .clip(RoundedCornerShape(20.dp))
                                        .clickable {
                                            selectProductVariant.invoke(
                                                productVariantHolder
                                            )
                                        }
                                )
                                {
                                    Box(
                                        Modifier
                                            .padding(2.dp)
                                            .height(22.dp)
                                            .width(22.dp)
                                            .background(
                                                colorValue,
                                                RoundedCornerShape(20.dp)
                                            )

                                    ) { }
                                }
                        }

                        else -> {
                            Box(
                                modifier = Modifier
                                    .border(
                                        1.dp,
                                        if (selectedProductVariant.contains(productVariantHolder))
                                            CustomColor.primaryColor700 else CustomColor.neutralColor200,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 10.dp, vertical = 10.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable { selectProductVariant.invoke(productVariantHolder) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = productVariants[index][pvIndex].name,
                                    color = CustomColor.neutralColor950,
                                    fontFamily = General.satoshiFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    // modifier = Modifier.padding(start = 15.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}