package com.ericktijerou.anticucho_detector.android.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun CircleAnimatedButton(
    modifier: Modifier, strokeWidth: Dp,
    onClick: () -> Unit
) {
    Pulsating(modifier, onClick = onClick) { selected ->
        CircleButton(modifier, strokeWidth, selected)
    }
}

@Composable
fun CircleButton(
    modifier: Modifier,
    strokeWidth: Dp,
    selected: Boolean
) {
    val strokePx = LocalDensity.current.run { strokeWidth.toPx() }
    Canvas(modifier) {
        drawCircle(
            color = Color.White,
            radius = size.minDimension / 4,
            center = Offset(size.width / 2, size.height / 2),
            style = Stroke(width = strokePx)
        )
        if (selected) {
            drawCircle(
                color = Color.Red,
                center = Offset(x = size.width / 2, y = size.height / 2),
                radius = size.minDimension / 4 - strokePx * 1.5f
            )
        }
    }
}