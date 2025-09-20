package smartcaravans.constat.client.core.presentation

import android.content.Context
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import smartcaravans.constat.client.ui.theme.AppTheme
import java.io.File
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun SimpleCameraView(
    modifier: Modifier = Modifier,
    onPhotoCaptured: (File) -> Unit = { _ -> }
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
    var camera: Camera? by remember { mutableStateOf(null) }
    var isFlashOn by remember { mutableStateOf(false) }
    val previewView = remember { PreviewView(context) }

    LaunchedEffect(Unit) {
        val cameraProvider = context.getCameraProvider()

        val preview = Preview.Builder().build().also {
            it.surfaceProvider = previewView.surfaceProvider
        }

        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture
            )
        } catch (_: Exception) {
            // Handle error
        }
    }

    // Flash control effect
    LaunchedEffect(isFlashOn) {
        camera?.cameraControl?.enableTorch(isFlashOn)
    }

    Box(modifier = modifier.fillMaxSize()) {
        // Camera Preview
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )

        // Instruction text
        Text(
            text = "Appuyez sur le déclencheur pour prendre une photo",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 80.dp)
                .padding(horizontal = 32.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium
        )

        // Shutter and Flash buttons at bottom
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-40).dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Flash Button
            Surface(
                modifier = Modifier
                    .size(60.dp)
                    .alpha(0.8f),
                border = BorderStroke(2.dp, Color.White),
                shape = CircleShape,
                color = Color.Black.copy(alpha = 0.3f)
            ) {
                Box(
                    Modifier
                        .clip(CircleShape)
                        .clickable {
                            isFlashOn = !isFlashOn
                        }
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isFlashOn) Icons.Filled.FlashOn else Icons.Filled.FlashOff,
                        contentDescription = if (isFlashOn) "Flash éteint" else "Flash allumé",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Shutter Button
            Surface(
                modifier = Modifier
                    .size(80.dp),
                border = BorderStroke(4.dp, Color.White),
                shape = CircleShape,
                color = Color.Transparent
            ) {
                Box(
                    Modifier
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable {
                            captureSimplePhoto(
                                imageCapture = imageCapture,
                                outputDirectory = context.filesDir,
                                executor = ContextCompat.getMainExecutor(context),
                                onPhotoCaptured = onPhotoCaptured,
                                onError = { /* Handle error if needed */ }
                            )
                        }
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }
        }
    }
}

private suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { cameraProvider ->
        cameraProvider.addListener({
            continuation.resume(cameraProvider.get())
        }, ContextCompat.getMainExecutor(this))
    }
}

private fun captureSimplePhoto(
    imageCapture: ImageCapture?,
    outputDirectory: File,
    executor: Executor,
    onPhotoCaptured: (File) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {
    val photoFile = File(
        outputDirectory,
        "Photo_${System.currentTimeMillis()}.jpg"
    )

    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture?.takePicture(
        outputOptions,
        executor,
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                onPhotoCaptured(photoFile)
            }

            override fun onError(exception: ImageCaptureException) {
                onError(exception)
            }
        }
    )
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
private fun SimpleCameraPreview() {
    var result by remember { mutableStateOf<File?>(null) }
    AppTheme {
        Column {
            SimpleCameraView(Modifier.weight(1f).fillMaxSize()) { file ->
                result = file
            }
            Text(
                "Dernier fichier: ${result?.absolutePath ?: "Aucun"}",
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(16.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}
