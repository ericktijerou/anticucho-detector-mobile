package com.ericktijerou.anticucho_detector.android.ui

import android.Manifest
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.registerForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ericktijerou.anticucho_detector.Greeting
import com.ericktijerou.anticucho_detector.android.BuildConfig.APPLICATION_ID
import com.ericktijerou.anticucho_detector.android.R
import com.ericktijerou.anticucho_detector.android.ui.theme.AnticuchoDetectorTheme
import org.koin.androidx.compose.getViewModel
import org.osmdroid.config.Configuration
import java.io.File

fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().userAgentValue = APPLICATION_ID

        setContent {
            AnticuchoDetectorTheme {
                // A surface container using the 'background' color from the theme
                Surface(Modifier.fillMaxSize()) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val permissionGranted = remember { mutableStateOf(false) }
    val viewModel = getViewModel<AnticuchoDetectorViewModel>()
    val timerVisibility: String by viewModel.upload.observeAsState("")

    Box(modifier = Modifier.fillMaxSize()) {
        SimpleCameraPreview(
            viewModel.setupImageCapture(0, getOutputDirectory(LocalContext.current))
        )
        Button(onClick = { viewModel.setGestureCode() }, modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)) {
            Text("TAKE")
        }
        ImageButton(viewModel.captureFileUri) { viewModel.viewImage(it) }

    }
    RequestPermission(Manifest.permission.CAMERA) { granted ->
        if (granted) {
            permissionGranted.value = true
        }
    }
}

@Composable
fun RequestPermission(permission: String, onPermissionGranted: (Boolean) -> Unit) {
    val l = registerForActivityResult(
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