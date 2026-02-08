package com.example.eccomerce_app.ui.component

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.SubcomposeAsyncImage
import com.example.e_commercompose.R
import com.example.eccomerce_app.util.General
import com.example.e_commercompose.ui.theme.CustomColor
import com.example.eccomerce_app.util.General.toCustomFil

@Composable
fun CreateProductImage(
    thumbnail: String?,
    images: List<String>?,
    deleteImages: List<String>,
    context: Any,
    onSelectThumbnail: (value: String) -> Unit,
    onSelectImages: (value: List<String>) -> Unit,
    onRemoveAlreadyProductImage: (value: List<String>) -> Unit
) {
    val androidContext = context as Context

    val onImageSelection = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    )
    { uri ->
        if (uri != null) {
            val fileHolder = uri.toCustomFil(context = androidContext)
            if (fileHolder != null) {
                // fileHolder is Any? (Uri or File?). toCustomFil returns Any?
                // The implementation in AndroidGeneral returns File? so it might need casting or toString.
                // Assuming it returns File (with absolutePath).
                // Wait, previous code used fileHolder.absolutePath.
                // So fileHolder must be File.
                // AndroidGeneral.toCustomFil returns File?
                val file = fileHolder as? java.io.File
                if (file != null) {
                    if (thumbnail != null && !deleteImages.contains(thumbnail)) {
                        val deleteImageCopy = mutableListOf<String>()
                        deleteImageCopy.add(thumbnail)
                        deleteImageCopy.addAll(deleteImages)
                        onRemoveAlreadyProductImage(deleteImageCopy)
                    }
                    onSelectThumbnail(file.absolutePath)
                }
            }
        }
    }

    val selectMultipleImages = rememberLauncherForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia(10)
    )
    { uris ->
        val imagesHolder = mutableListOf<String>()

        if (uris.isNotEmpty()) {
            uris.forEach { productImages ->
                val fileHolder = productImages.toCustomFil(androidContext)
                if (fileHolder != null) {
                    imagesHolder.add(fileHolder.absolutePath)
                }
            }
            onSelectImages(imagesHolder)
        }
    }


    Column() {
        Text(
            stringResource(R.string.product_thumbnail),
            fontFamily = General.satoshiFamily,
            fontWeight = FontWeight.Bold,
            fontSize = (18).sp,
            color = CustomColor.neutralColor950,
            textAlign = TextAlign.Center,
        )
        Sizer(15)
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
        )
        {
            val (imageRef, cameralRef) = createRefs()
            Box(
                modifier = Modifier
                    .constrainAs(imageRef) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .height(150.dp)
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = CustomColor.neutralColor500,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .background(
                        color = Color.White,
                    ),
                contentAlignment = Alignment.Center
            )
            {
                when (thumbnail == null) {
                    true -> {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.insert_photo),
                            "",
                            modifier = Modifier.size(80.dp),
                            tint = CustomColor.neutralColor200
                        )
                    }

                    else -> {
                        SubcomposeAsyncImage(
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp)),
                            model = General.handlingImageForCoil(
                                thumbnail,
                                androidContext
                            ),
                            contentDescription = "",
                            loading = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    contentAlignment = Alignment.Center // Ensures the loader is centered and doesn't expand
                                ) {
                                    CircularProgressIndicator(
                                        color = Color.Black,
                                        modifier = Modifier.size(54.dp) // Adjust the size here
                                    )
                                }
                            },
                        )
                    }
                }

            }
            Box(
                modifier = Modifier
                    .padding(end = 5.dp, bottom = 10.dp)
                    .constrainAs(cameralRef) {
                        end.linkTo(imageRef.end)
                        bottom.linkTo(imageRef.bottom)
                    }


            )
            {

                IconButton(
                    onClick = {
                        onImageSelection.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    },
                    modifier = Modifier
                        .size(30.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = CustomColor.primaryColor200
                    )
                ) {
                    Icon(
                        ImageVector.vectorResource(R.drawable.camera),
                        "",
                        modifier = Modifier.size(18.dp),
                        tint = Color.White
                    )
                }
            }

        }
        Sizer(10)
        Text(
            stringResource(R.string.product_images),
            fontFamily = General.satoshiFamily,
            fontWeight = FontWeight.Bold,
            fontSize = (18).sp,
            color = CustomColor.neutralColor950,
            textAlign = TextAlign.Center,
        )
        Sizer(5)
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
        )
        {
            val (cameralRef) = createRefs()

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = CustomColor.neutralColor500,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 5.dp, vertical = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalArrangement = Arrangement.spacedBy(5.dp)

            ) {
                if (images.isNullOrEmpty()) {
                    Box(
                        modifier = Modifier
                            .height(150.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.insert_photo),
                            "",
                            modifier = Modifier.size(80.dp),
                            tint = CustomColor.neutralColor200
                        )
                    }
                }

                images!!.forEach { value ->

                    ConstraintLayout {
                        Box(
                            modifier = Modifier

                                .height(120.dp)
                                .width(120.dp)
                                .background(
                                    color = Color.White,
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            SubcomposeAsyncImage(
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp)),
                                model = General.handlingImageForCoil(
                                    value,
                                    androidContext
                                ),
                                contentDescription = "",
                                loading = {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize(),
                                        contentAlignment = Alignment.Center // Ensures the loader is centered and doesn't expand
                                    ) {
                                        CircularProgressIndicator(
                                            color = Color.Black,
                                            modifier = Modifier.size(54.dp) // Adjust the size here
                                        )
                                    }
                                },
                            )

                        }

                        Box(
                            modifier = Modifier
                                .height(30.dp)
                                .width(30.dp)
                                .background(
                                    Color.Red,
                                    RoundedCornerShape(20.dp)
                                )
                                .clip(
                                    RoundedCornerShape(20.dp)
                                )
                                .clickable {
                                    onRemoveAlreadyProductImage(images.filter { it != value })
                                },
                            contentAlignment = Alignment.Center
                        )
                        {
                            Icon(
                                Icons.Default.Clear, "",
                                tint = Color.White
                            )
                        }
                    }

                }
            }


            Box(
                modifier = Modifier
                    .padding(end = 5.dp, bottom = 10.dp)
                    .constrainAs(cameralRef) {
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
            )
            {

                IconButton(
                    onClick = {
                        selectMultipleImages.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    },
                    modifier = Modifier
                        .size(30.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = CustomColor.primaryColor200
                    )
                ) {
                    Icon(
                        ImageVector.vectorResource(R.drawable.camera),
                        "",
                        modifier = Modifier.size(18.dp),
                        tint = Color.White
                    )
                }
            }

        }

    }
}
