package com.ericktijerou.anticucho_detector.android.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import java.io.File
import java.util.*

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

fun String.capitalizeFirstLetter(): String {
    return this.split(" ").joinToString(" ") { str ->
        str.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }
    }.trimEnd()
}
