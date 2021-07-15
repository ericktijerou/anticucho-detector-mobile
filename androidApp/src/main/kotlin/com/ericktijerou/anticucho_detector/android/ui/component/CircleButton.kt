package com.ericktijerou.anticucho_detector.android.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ericktijerou.anticucho_detector.android.ui.theme.Teal200

@Composable
fun TakePhotoButton(
    modifier: Modifier, strokeWidth: Dp,
    onClick: () -> Unit
) {
    Pulsating(modifier, onClick = onClick) { selected ->
        CustomCircleButton(modifier, strokeWidth, selected)
    }
}

@Composable
fun CustomCircleButton(
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

@Composable
fun AnalyzeProgressButton(modifier: Modifier, loading: Boolean) {
    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        val strokePx = LocalDensity.current.run { 2.dp.toPx() }
        Canvas(Modifier.fillMaxSize()) {
            drawCircle(
                color = Color.Black,
                radius = size.minDimension / 2 - strokePx,
                center = Offset(size.width / 2, size.height / 2),
            )
            if (!loading) {
                drawCircle(
                    color = Color.White,
                    radius = size.minDimension / 2,
                    center = Offset(size.width / 2, size.height / 2),
                    style = Stroke(width = strokePx)
                )
            }
        }
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                strokeWidth = 2.dp,
                color = Color.White
            )
        }
        Icon(
            imageVector = Icons.Filled.Send,
            contentDescription = "Start",
            tint = Color.White
        )
    }
}