package com.ericktijerou.anticucho_detector.android.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import java.io.File

inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit) = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

fun File.createDirIfNotExists() = apply {
    if (!exists()) {
        mkdir()
    }
}

fun Any?.isNull(): Boolean = (this == null)

fun Any?.isNotNull(): Boolean = (this != null)

fun Int?.orZero(): Int = this ?: 0
