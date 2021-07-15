package com.ericktijerou.anticucho_detector.android.ui.result

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.ericktijerou.anticucho_detector.android.util.SEPARATOR
import com.ericktijerou.anticucho_detector.android.util.capitalizeFirstLetter

@Composable
fun ResultScreen(upPress: () -> Unit, result: String) {
    val nameList = result.split(SEPARATOR).toList()
    Column {
        nameList.forEach { name ->
            Text(
                name.replace("_", " ").capitalizeFirstLetter()
            )
        }
    }
}