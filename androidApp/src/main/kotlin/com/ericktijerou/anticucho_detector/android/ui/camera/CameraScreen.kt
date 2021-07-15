package com.ericktijerou.anticucho_detector.android.ui.camera

import android.Manifest
import android.content.Context
import android.util.Size
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.ericktijerou.anticucho_detector.android.R
import com.ericktijerou.anticucho_detector.android.ui.AnticuchoDetectorViewModel
import com.ericktijerou.anticucho_detector.android.ui.component.AnalyzeProgressButton
import com.ericktijerou.anticucho_detector.android.ui.component.CapturedImageView
import com.ericktijerou.anticucho_detector.android.ui.component.SimpleCameraPreview
import com.ericktijerou.anticucho_detector.android.ui.component.TakePhotoButton
import com.ericktijerou.anticucho_detector.android.ui.theme.DetectorTheme
import com.ericktijerou.anticucho_detector.android.util.*
import kotlinx.coroutines.delay
import org.koin.androidx.compose.getViewModel
import java.io.File

@Composable
fun CameraScreen(goToResult: (String) -> Unit) {
    val permissionGranted = remember { mutableStateOf(false) }
    val uploading = remember { mutableStateOf(false) }
    val viewModel = getViewModel<AnticuchoDetectorViewModel>()
    val (isFlashEnabled, startFlashEffect) = remember { mutableStateOf(false) }
    val openDialog = remember { mutableStateOf(false) }
    val result: List<String>? by viewModel.upload.observeAsState(null)

    val onClearResults = {
        openDialog.value = false
        uploading.value = false
        viewModel.clearCapturedImage()
    }

    if (result?.size.orZero() > 0) {
        goToResult(Screen.Result.route(result?.joinToString(SEPARATOR).orEmpty()))
        onClearResults()
    } else if (result.isNotNull() && viewModel.captureFileUri.isNotNull()) {
        uploading.value = false
        openDialog.value = true
    }

    val borderWidth = if (isFlashEnabled) 20.dp else 0.dp
    if (permissionGranted.value) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
                .border(BorderStroke(borderWidth, Color.White))
        ) {
            val maxWidthPx = LocalDensity.current.run { maxWidth.toPx().toInt() / 2 }
            val maxHeightPx = LocalDensity.current.run { maxHeight.toPx().toInt() / 2 }
            viewModel.captureFileUri?.let { uri ->
                CapturedImageView(uri) { viewModel.viewImage(it) }
                AnalyzeProgressButton(
                    modifier = Modifier
                        .padding(20.dp)
                        .align(Alignment.BottomEnd)
                        .size(64.dp)
                        .noRippleClickable {
                            uploading.value = true
                            viewModel.uploadImage()
                        },
                    loading = uploading.value
                )
            } ?: run {
                SimpleCameraPreview(
                    viewModel.setupImageCapture(
                        0,
                        getOutputDirectory(LocalContext.current),
                        Size(maxWidthPx, maxHeightPx)
                    ),
                    Modifier.fillMaxSize()
                )
                TakePhotoButton(
                    modifier = Modifier.align(Alignment.BottomCenter).size(140.dp),
                    strokeWidth = 3.dp,
                    onClick = {
                        viewModel.takePicture()
                        startFlashEffect(true)
                        uploading.value = false
                    }
                )
            }
        }
    }

    NoFacesErrorDialog(
        openDialog = openDialog.value,
        modifier = Modifier.padding(16.dp),
        onDismissRequest = onClearResults
    )

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
    BackHandler(
        onBack = onClearResults
    )
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

@Composable
fun NoFacesErrorDialog(modifier: Modifier, openDialog: Boolean, onDismissRequest: () -> Unit) {
    if (openDialog) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = {
                Text(text = "No faces found")
            },
            text = {
                Text("No faces could be found in this photo. Please try another one!")
            },
            confirmButton = {
                TextButton(onClick = onDismissRequest) {
                    Text("OK", color = Color.White)
                }
            },
            modifier = modifier,
            properties = DialogProperties(dismissOnClickOutside = false),
            backgroundColor = DetectorTheme.colors.background
        )
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