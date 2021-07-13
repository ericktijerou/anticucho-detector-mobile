package com.ericktijerou.anticucho_detector.android.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import com.ericktijerou.anticucho_detector.android.BuildConfig.APPLICATION_ID
import com.ericktijerou.anticucho_detector.android.util.LocalBackDispatcher
import com.ericktijerou.anticucho_detector.android.util.LocalSysUiController
import com.ericktijerou.anticucho_detector.android.util.SystemUiController
import org.osmdroid.config.Configuration

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().userAgentValue = APPLICATION_ID
        setContent {
            val systemUiController = remember { SystemUiController(window) }
            CompositionLocalProvider(LocalSysUiController provides systemUiController, LocalBackDispatcher provides onBackPressedDispatcher) {
                DetectorApp()
            }
        }
    }
}