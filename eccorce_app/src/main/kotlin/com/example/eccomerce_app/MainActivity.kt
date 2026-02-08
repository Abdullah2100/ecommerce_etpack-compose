package com.example.eccomerce_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.eccomerce_app.util.General.currentLocal
import com.example.eccomerce_app.util.AndroidGeneral
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
               AndroidGeneral.whenLanguageUpdateDo(value?:"ar",this@MainActivity)
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
            App()
        }
    }


    override fun onDestroy() {
//        connection?.stop()
        super.onDestroy()
    }
}
