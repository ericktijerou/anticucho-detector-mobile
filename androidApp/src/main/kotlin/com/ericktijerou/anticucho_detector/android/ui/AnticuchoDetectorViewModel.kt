package com.ericktijerou.anticucho_detector.android.ui

import android.net.Uri
import android.util.Log
import android.util.Size
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.*
import co.touchlab.kermit.Kermit
import com.ericktijerou.anticucho_detector.repository.AnticuchoDetectorRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

class AnticuchoDetectorViewModel(
    private val detectorRepository: AnticuchoDetectorRepository,
    private val logger: Kermit
) : ViewModel() {

    private val _upload = MutableLiveData<Boolean>()
    val upload = Transformations.switchMap(_upload) {
        liveData(handler) {
            if (it) {
                val result = compareImage()
                emit(result)
            } else {
                emit(null)
            }
        }
    }

    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.e("ERROR", exception.message.orEmpty())
    }

    val peopleInSpace = detectorRepository.fetchPeopleAsFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private var capturing = false

    var captureFileUri by mutableStateOf<Uri?>(null)
        private set

    private lateinit var imageCapture: ImageCapture
    private lateinit var outputFolder: File

    fun viewImage(uri: Uri) {
        Log.d(TAG, "view Image: $uri")
    }

    fun setupImageCapture(rotation: Int, outputFolder: File, resolution: Size): ImageCapture {
        if (!this::imageCapture.isInitialized) {
            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .setTargetResolution(resolution)
                .setTargetRotation(rotation)
                .build()
            this.outputFolder = outputFolder
        }
        Log.d(TAG, "return imageCapture: $imageCapture")
        return imageCapture
    }

    fun takePicture() {
        runBlocking {
            if (!capturing) {
                capturing = true
                capture()
            }
        }
    }

    private suspend fun compareImage(): List<String> {
        val file = File(captureFileUri?.path.orEmpty())
        return detectorRepository.compareImage(filename = file.name, file.absolutePath)
    }

    private fun capture() {
        // Create output file to hold the image
        val photoFile = createFile(outputFolder, FILENAME, PHOTO_EXTENSION)

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile)
            .build()

        imageCapture.takePicture(outputOptions, Executors.newSingleThreadExecutor(),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    capturing = false
                    captureFileUri = output.savedUri ?: Uri.fromFile(photoFile)
                    Log.d(TAG, "Photo capture succeeded: $captureFileUri")
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG, "Photo capture exception: $exception")
                }
            })
    }

    fun clearCapturedImage() {
        captureFileUri = null
        _upload.postValue(false)
    }

    fun uploadImage() {
        _upload.postValue(true)
    }

    companion object {
        private const val TAG = "GestureViewModel"
        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_EXTENSION = ".jpg"

        /** Helper function used to create a timestamped file */
        private fun createFile(baseFolder: File, format: String, extension: String) =
            File(
                baseFolder, SimpleDateFormat(format, Locale.US)
                    .format(System.currentTimeMillis()) + extension
            )
    }
}