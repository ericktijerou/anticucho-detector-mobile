package com.ericktijerou.anticucho_detector.android.ui.component

import android.media.MediaScannerConnection
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toFile
import com.ericktijerou.anticucho_detector.android.util.noRippleClickable
import com.google.accompanist.coil.rememberCoilPainter

private const val TAG = "ImageButton"

@Composable
fun CapturedImageView(uri: Uri, imageClicked: (Uri) -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = rememberCoilPainter(
                uri,
                fadeIn = true
            ),
            contentDescription = "Captured Image",
            modifier = Modifier.fillMaxSize().noRippleClickable {
                imageClicked.invoke(uri)
            }
        )

        val mimeType = MimeTypeMap.getSingleton()
            .getMimeTypeFromExtension(uri.toFile().extension)
        MediaScannerConnection.scanFile(
            LocalContext.current,
            arrayOf(uri.toFile().absolutePath),
            arrayOf(mimeType)
        ) { _, uri ->
            Log.d(TAG, "Image capture scanned into media store: $uri")
        }
    }
}