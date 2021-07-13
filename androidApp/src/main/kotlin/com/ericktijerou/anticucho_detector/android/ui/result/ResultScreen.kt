package com.ericktijerou.anticucho_detector.android.ui.result

import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun ResultScreen(upPress: () -> Unit, result: String) {
    Text(result)
}