package com.ericktijerou.anticucho_detector.android.ui

import androidx.compose.runtime.Composable
import com.ericktijerou.anticucho_detector.android.ui.theme.DetectorTheme
import com.ericktijerou.anticucho_detector.android.util.NavGraph
import com.ericktijerou.anticucho_detector.android.util.Screen
import com.google.accompanist.insets.ProvideWindowInsets

@Composable
fun DetectorApp() {
    ProvideWindowInsets {
        DetectorTheme {
            NavGraph(startDestination = Screen.Camera)
        }
    }
}
