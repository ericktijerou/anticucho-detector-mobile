package com.ericktijerou.anticucho_detector.android.ui.result

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ericktijerou.anticucho_detector.android.ui.theme.DetectorTheme
import com.ericktijerou.anticucho_detector.android.ui.theme.typography
import com.ericktijerou.anticucho_detector.android.util.SEPARATOR
import com.ericktijerou.anticucho_detector.android.util.capitalizeFirstLetter

@Composable
fun ResultScreen(upPress: () -> Unit, result: String) {
    val nameList = result.split(SEPARATOR).toList()
    Column(modifier = Modifier.fillMaxSize()) {
        IconButton(
            onClick = upPress,
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            nameList.forEach { name ->
                Text(
                    name.replace("_", " ").capitalizeFirstLetter(),
                    color = Color.White,
                    style = DetectorTheme.typography.h4
                )
            }
        }
    }
}