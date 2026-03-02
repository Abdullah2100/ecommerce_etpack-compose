package com.example.e_commerce_delivery_man.ui.view.home

import android.annotation.SuppressLint
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.e_commerce_delivery_man.viewModel.OrderViewModel
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.e_commerce_delivery_man.services.kSerializeChanger.ImageAnalyser
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID


@SuppressLint("SuspiciousIndentation")
@Composable
fun QrScannerPage(
    nav: NavHostController,
    orderViewModel: OrderViewModel,
) {

    val lifecycleOwner = LocalLifecycleOwner.current

    val coroutine = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }


    val isOpenDialog = remember { mutableStateOf(false) }
    val orderId = remember { mutableStateOf<UUID?>(null) }
    val isSendingData = remember { mutableStateOf(false) }
    val order = rememberUpdatedState{orderViewModel.myOrders.value?.firstOrNull { it.id == orderId.value }}

    fun updateStatus(id: UUID) {
        coroutine.launch {
            isSendingData.value = true
            isOpenDialog.value = false
            delay(100)
            val result = async {
                orderViewModel.updateStatus(id)
            }.await()
            isOpenDialog.value = false

            isSendingData.value = false
            if (!result.isNullOrEmpty()) {
                isOpenDialog.value = false
                snackBarHostState.showSnackbar(result)

                return@launch;
            } else {
                snackBarHostState.showSnackbar("Order Received Successfully")
            }

        }

    }




    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }
    ) { paddingValues ->
        paddingValues.calculateTopPadding()

        paddingValues.calculateBottomPadding()

        if (isOpenDialog.value)
            Dialog(
                onDismissRequest = { isOpenDialog.value = !isOpenDialog.value }
            )
            {
                Column (
                    modifier = Modifier
                        .height(90.dp)
                        .width(70.dp)
                        .background(Color.White, RoundedCornerShape(20.dp)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center

                ) {
                    //CircularProgressIndicator()
                    when(order.value.invoke()?.isAlreadyPayed ==false){
                        true->{
                            Text("Collect Money From User ")
                            Text("${order.value}")
                        }

                        else -> {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        AndroidView(
            factory = { context ->
                val cameraProvider = ProcessCameraProvider.getInstance(context)
                val previewView = PreviewView(context)
                val preview = Preview.Builder().build()
                val selector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()

                preview.surfaceProvider = previewView.surfaceProvider

                val imageAnalyser = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                if (!isOpenDialog.value)
                    imageAnalyser.setAnalyzer(
                        ContextCompat.getMainExecutor(context),
                        ImageAnalyser { qrCode ->
                            isOpenDialog.value = true
                            val id  = UUID.fromString(qrCode)
                            orderViewModel.myOrders.value?.firstOrNull { it.id == id }
                                .let { orderHolder ->
                                    if (orderHolder!!.isAlreadyPayed) {
                                        updateStatus(id)
                                    }
                                }
                        }
                    )

                try {
                    cameraProvider.get().bindToLifecycle(
                        lifecycleOwner,
                        selector,
                        preview,
                        imageAnalyser
                    )
                } catch (e: Exception) {

                }

                previewView
            },
            modifier = Modifier.fillMaxWidth()
        )


    }
}