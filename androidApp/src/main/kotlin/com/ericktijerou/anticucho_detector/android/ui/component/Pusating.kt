package com.ericktijerou.anticucho_detector.android.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import com.ericktijerou.anticucho_detector.android.util.noRippleClickable
import kotlinx.coroutines.delay

@Composable
fun Pulsating(
    modifier: Modifier,
    pulseFraction: Float = 1.4f,
    onClick: () -> Unit,
    content: @Composable (Boolean) -> Unit
) {
    val selected = remember { mutableStateOf(false) }
    val scale = animateFloatAsState(if (selected.value) pulseFraction else 1f)
    Box(modifier = modifier.scale(scale.value).noRippleClickable {
        onClick()
        selected.value = true
    }) {
        content(selected.value)
    }
    LaunchedEffect(selected.value) {
        if (selected.value) {
            delay(150)
            selected.value = !selected.value
        }
    }
}