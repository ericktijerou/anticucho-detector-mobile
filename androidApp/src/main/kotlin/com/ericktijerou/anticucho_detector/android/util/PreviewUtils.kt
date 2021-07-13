package com.ericktijerou.anticucho_detector.android.util

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.ericktijerou.anticucho_detector.android.ui.theme.DetectorTheme

@Composable
internal fun ThemedPreview(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    DetectorTheme(darkTheme = darkTheme) {
        Surface {
            content()
        }
    }
}
