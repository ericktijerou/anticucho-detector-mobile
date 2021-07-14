package com.ericktijerou.anticucho_detector.android.ui.camera

import android.Manifest
import android.content.Context
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ericktijerou.anticucho_detector.android.R
import com.ericktijerou.anticucho_detector.android.ui.AnticuchoDetectorViewModel
import com.ericktijerou.anticucho_detector.android.ui.component.CapturedImageView
import com.ericktijerou.anticucho_detector.android.ui.component.CircleAnimatedButton
import com.ericktijerou.anticucho_detector.android.ui.component.SimpleCameraPreview
import kotlinx.coroutines.delay
import org.koin.androidx.compose.getViewModel
import java.io.File

@Composable
fun CameraScreen(goToResult: (String) -> Unit) {
    val permissionGranted = remember { mutableStateOf(false) }
    val viewModel = getViewModel<AnticuchoDetectorViewModel>()
    val (isFlashEnabled, startFlashEffect) = remember { mutableStateOf(false) }
    val result: String by viewModel.upload.observeAsState("")

    //if (result.isNotEmpty()) goToResult(Screen.Result.route(result))
    val borderWidth = if (isFlashEnabled) 20.dp else 0.dp
    if (permissionGranted.value) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize().border(BorderStroke(borderWidth, Color.White))
        ) {
            val maxWidthPx = LocalDensity.current.run { maxWidth.toPx().toInt() / 2 }
            val maxHeightPx = LocalDensity.current.run { maxHeight.toPx().toInt() / 2 }
            viewModel.captureFileUri?.let { uri ->
                CapturedImageView(uri) { viewModel.viewImage(it) }
            } ?: run {
                SimpleCameraPreview(
                    viewModel.setupImageCapture(
                        0,
                        getOutputDirectory(LocalContext.current),
                        Size(maxWidthPx, maxHeightPx)
                    ),
                    Modifier.fillMaxSize()
                )
            }
            CircleAnimatedButton(
                modifier = Modifier.align(Alignment.BottomCenter).size(140.dp),
                strokeWidth = 3.dp,
                onClick = {
                    viewModel.setGestureCode()
                    startFlashEffect(true)
                }
            )
        }
    }

    RequestPermission(Manifest.permission.CAMERA) { granted ->
        if (granted) {
            permissionGranted.value = true
        }
    }

    LaunchedEffect(isFlashEnabled) {
        if (isFlashEnabled) {
            delay(150)
            startFlashEffect(false)
        }
    }
}

@Composable
fun RequestPermission(permission: String, onPermissionGranted: (Boolean) -> Unit) {
    val l = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { result ->
            onPermissionGranted(result.all { it.value })
        }
    )
    SideEffect {
        l.launch(arrayOf(permission))
    }
}

private fun getOutputDirectory(context: Context): File {
    val dir = File(context.filesDir, "captured").createDirIfNotExists()
    val appContext = context.applicationContext
    val mediaDir = dir.let {
        File(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() }
    }
    return if (mediaDir.exists())
        mediaDir else appContext.filesDir
}

fun File.createDirIfNotExists() = apply {
    if (!exists()) {
        mkdir()
    }
}


@Composable
fun MainContent() {
    val isEnabled = remember { mutableStateOf(true) }
    val isCollapsed = remember { mutableStateOf(true) }

    val size: Dp by animateDpAsState(
        targetValue = if (isCollapsed.value) 100.dp else 300.dp,
        animationSpec = tween(
            durationMillis = 3000, // duration
            delayMillis = 2000, // delay before start animation
            easing = FastOutSlowInEasing
        ),
        finishedListener = {
            // disable the button
            isEnabled.value = true
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFDDF4))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                isCollapsed.value = !isCollapsed.value
                isEnabled.value = false
            },
            colors = ButtonDefaults.buttonColors(
                Color(0xFF2A2F23), Color(0xCCFFFFFF)
            ),
            enabled = isEnabled.value
        ) {
            Text(
                text = "Animate Size Change",
                modifier = Modifier.padding(12.dp)
            )
        }

        Icon(
            Icons.Filled.Favorite,
            contentDescription = "Localized description",
            Modifier.size(size),
            tint = Color(0xFFFF355E)
        )
    }
}