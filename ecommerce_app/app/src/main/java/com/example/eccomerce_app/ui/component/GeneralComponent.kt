package com.example.eccomerce_app.ui.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.e_commercompose.ui.theme.CustomColor
import com.example.eccomerce_app.util.General
import java.util.UUID


@Composable
fun Sizer(heigh:Int=0,width:Int=0,color: Color=Color.White){
    Box(
      modifier = Modifier
          .height((heigh).dp)
          .width((width.dp))
          .background(color)
    )
}

@Composable
fun SwapToDismiss(
    component: @Composable () -> Unit, index: UUID, delete: (id: UUID) -> Unit
) {

    val swipeState = rememberSwipeToDismissBoxState()

    LaunchedEffect(swipeState.currentValue) {
        if (swipeState.currentValue == SwipeToDismissBoxValue.EndToStart) {
            delete(index)
        }
    }
    SwipeToDismissBox(
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        state = swipeState,
        content = {
            component()
        },
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize()
                    .background(CustomColor.alertColor_1_400),
                contentAlignment = Alignment.CenterEnd
            ) {
                IconButton(
                    onClick = {}) {
                    Icon(
                        Icons.Default.Delete,
                        "",
                        tint = CustomColor.alertColor_1_600,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        },
        gesturesEnabled = true,

        )
}


@Composable
fun CustomDropDownComponent(
    value:String,
    items:List<String>?=null,
    onSelectValue:(value: String)-> Unit
){
    val isExpanded = remember{ mutableStateOf(false) }
    val animated = animateDpAsState(if (isExpanded.value) ((items?.size ?: 1) * 45).dp else 0.dp)
    val rotation = animateFloatAsState(if (isExpanded.value) 180f else 0f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                CustomColor.neutralColor400,
                RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
    )
    {

        Row(
            modifier = Modifier
                .height(65.dp)
                .fillMaxWidth()

                .clickable {
                    isExpanded.value = !isExpanded.value
                }
                .padding(horizontal = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Text(value)
            Icon(
                Icons.Default.KeyboardArrowDown, "",
                modifier = Modifier.rotate(rotation.value)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(animated.value)
                .border(
                    1.dp,
                    CustomColor.neutralColor200,
                    RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 8.dp,
                        bottomEnd = 8.dp
                    )
                ),

            )
        {
            items?.forEach { value ->
                Text(
                    value,
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            onSelectValue(value)
                            isExpanded.value = !isExpanded.value
                        }
                        .padding(top = 12.dp, start = 5.dp)

                )
            }
        }
    }
}


@Composable
fun LabelValueRow(text:String, value:String){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text,
            fontFamily = General.satoshiFamily,
            fontWeight = FontWeight.Normal,
            fontSize = (16).sp,
            color = CustomColor.neutralColor950,
            textAlign = TextAlign.Center
        )
        Text(
            value,
            fontFamily = General.satoshiFamily,
            fontWeight = FontWeight.Bold,
            fontSize = (16).sp,
            color = CustomColor.neutralColor950,
            textAlign = TextAlign.Center
        )
    }
}