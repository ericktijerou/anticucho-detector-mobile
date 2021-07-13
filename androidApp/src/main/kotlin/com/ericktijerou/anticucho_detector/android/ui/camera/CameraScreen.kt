package com.ericktijerou.anticucho_detector.android.ui.camera

import android.Manifest
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ericktijerou.anticucho_detector.android.R
import com.ericktijerou.anticucho_detector.android.ui.AnticuchoDetectorViewModel
import com.ericktijerou.anticucho_detector.android.ui.component.ImageButton
import com.ericktijerou.anticucho_detector.android.ui.component.SimpleCameraPreview
import com.ericktijerou.anticucho_detector.android.util.Screen
import org.koin.androidx.compose.getViewModel
import java.io.File

@Composable
fun CameraScreen(goToResult: (String) -> Unit) {
    val permissionGranted = remember { mutableStateOf(false) }
    val viewModel = getViewModel<AnticuchoDetectorViewModel>()
    val result: String by viewModel.upload.observeAsState("")

    if (result.isNotEmpty()) goToResult(Screen.Result.route(result))

    if (permissionGranted.value) {
        Box(modifier = Modifier.fillMaxSize()) {
            SimpleCameraPreview(
                viewModel.setupImageCapture(0, getOutputDirectory(LocalContext.current)),
                Modifier.fillMaxSize()
            )
            Button(onClick = { viewModel.setGestureCode() }, modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)) {
                Text("TAKE")
            }
            ImageButton(viewModel.captureFileUri) { viewModel.viewImage(it) }

        }
    }

    RequestPermission(Manifest.permission.CAMERA) { granted ->
        if (granted) {
            permissionGranted.value = true
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