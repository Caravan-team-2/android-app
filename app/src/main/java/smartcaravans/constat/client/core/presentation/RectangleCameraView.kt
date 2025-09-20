package smartcaravans.constat.client.core.presentation

import android.content.Context
import androidx.annotation.OptIn
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import smartcaravans.constat.client.ui.theme.AppTheme
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun CameraView(
    modifier: Modifier = Modifier,
    onIdCardCaptured: (File, Boolean) -> Unit = { _, _ -> }
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
    var camera: Camera? by remember { mutableStateOf(null) }
    var isIdCardDetected by remember { mutableStateOf(false) }
    var detectionConfidence by remember { mutableStateOf(0f) }
    var hasAutoCaptured by remember { mutableStateOf(false) }
    var isFlashOn by remember { mutableStateOf(false) }
    val previewView = remember { PreviewView(context) }

    // Auto-capture when ID card is detected with high confidence
    LaunchedEffect(isIdCardDetected, detectionConfidence) {
        if (isIdCardDetected && detectionConfidence > 0.7f && !hasAutoCaptured) {
            hasAutoCaptured = true
            // Delay slightly for better image stability
            kotlinx.coroutines.delay(500)

            capturePhoto(
                imageCapture = imageCapture,
                outputDirectory = context.filesDir,
                executor = ContextCompat.getMainExecutor(context),
                onPhotoCaptured = { file ->
                    onIdCardCaptured(file, true)
                },
                onError = {

                }
            )
        }
    }

    // Animated border color based on detection
    val borderAlpha by animateFloatAsState(
        targetValue = if (isIdCardDetected) 1f else 0.7f,
        animationSpec = tween(300),
        label = "border_alpha"
    )

    LaunchedEffect(Unit) {
        val cameraProvider = context.getCameraProvider()

        val preview = Preview.Builder().build().also {
            it.surfaceProvider = previewView.surfaceProvider
        }

        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()

        // Image analysis for ID card detection
        val imageAnalyzer = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also { analysis ->
                analysis.setAnalyzer(
                    Executors.newSingleThreadExecutor(),
                    IdCardAnalyzer { detected, confidence ->
                        isIdCardDetected = detected
                        detectionConfidence = confidence
                    }
                )
            }

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture,
                imageAnalyzer
            ).also { cameraInstance ->
                camera = cameraInstance
            }
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

        // Semi-transparent overlay outside the rectangle
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
        )

        // Clear rectangle area (cut-out)
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(width = 300.dp, height = 190.dp)
                .background(Color.Transparent)
                .border(
                    width = 3.dp,
                    color = when {
                        isIdCardDetected && detectionConfidence > 0.7f -> Color.Green
                        isIdCardDetected -> Color.Yellow
                        else -> Color.White
                    }.copy(alpha = borderAlpha),
                    shape = RoundedCornerShape(12.dp)
                )
        )

        // Detection status text
        Text(
            text = when {
                hasAutoCaptured -> "Photo capturée automatiquement!"
                isIdCardDetected && detectionConfidence > 0.7f -> "ID Card détectée - Capture automatique en cours..."
                isIdCardDetected -> "ID Card détectée (${(detectionConfidence * 100).toInt()}%)"
                else -> "Positionnez votre carte d'identité dans le cadre"
            },
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
                            // TODO: Add flash control logic
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

            // Shutter Button - now for manual capture if needed
            Surface(
                modifier = Modifier
                    .size(80.dp)
                    .alpha(if (!hasAutoCaptured && isIdCardDetected && detectionConfidence > 0.7f) 1f else 0.6f),
                border = BorderStroke(4.dp, Color.White),
                shape = CircleShape,
                color = Color.Transparent
            ) {
                Box(
                    Modifier
                        .clip(CircleShape)
                        .background(
                            if (!hasAutoCaptured && isIdCardDetected && detectionConfidence > 0.7f)
                                MaterialTheme.colorScheme.primary
                            else Color.White
                        )
                        .clickable {
                            capturePhoto(
                                imageCapture = imageCapture,
                                outputDirectory = context.filesDir,
                                executor = ContextCompat.getMainExecutor(context),
                                onPhotoCaptured = { file ->
                                    onIdCardCaptured(file, false)
                                },
                                onError = {

                                }
                            )
                        }
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }
        }
    }
}

private class IdCardAnalyzer(
    private val onIdCardDetected: (Boolean, Float) -> Unit
) : ImageAnalysis.Analyzer {

    private val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private val imageLabeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

    private var lastAnalysisTime = 0L
    private val analysisInterval = 500L // Analyze every 500ms

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastAnalysisTime < analysisInterval) {
            imageProxy.close()
            return
        }
        lastAnalysisTime = currentTime

        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            // Use both text recognition and image labeling for better detection
            var textConfidence = 0f
            var labelConfidence = 0f
            var completedTasks = 0

            // Text recognition
            textRecognizer.process(image)
                .addOnSuccessListener { visionText ->
                    textConfidence = analyzeTextForIdCard(visionText.text)
                    completedTasks++
                    if (completedTasks == 2) {
                        val finalConfidence = maxOf(textConfidence, labelConfidence)
                        onIdCardDetected(finalConfidence > 0.3f, finalConfidence)
                    }
                }
                .addOnFailureListener {
                    completedTasks++
                    if (completedTasks == 2) {
                        val finalConfidence = labelConfidence
                        onIdCardDetected(finalConfidence > 0.3f, finalConfidence)
                    }
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }

            // Image labeling
            imageLabeler.process(image)
                .addOnSuccessListener { labels ->
                    labelConfidence = analyzeLabelsForIdCard(labels.map { it.text to it.confidence })
                    completedTasks++
                    if (completedTasks == 2) {
                        val finalConfidence = maxOf(textConfidence, labelConfidence)
                        onIdCardDetected(finalConfidence > 0.3f, finalConfidence)
                    }
                }
                .addOnFailureListener {
                    completedTasks++
                    if (completedTasks == 2) {
                        val finalConfidence = textConfidence
                        onIdCardDetected(finalConfidence > 0.3f, finalConfidence)
                    }
                }
        } else {
            imageProxy.close()
        }
    }

    private fun analyzeTextForIdCard(text: String): Float {
        val idCardKeywords = listOf(
            // French ID card keywords
            "carte", "identite", "identité", "nationale", "republique", "république",
            "francaise", "française", "nom", "prenom", "prénom", "naissance",
            // English ID card keywords
            "identity", "card", "license", "licence", "passport", "id",
            // Common patterns
            "birth", "date", "address", "nationality"
        )

        val textLower = text.lowercase()
        var matchCount = 0
        var totalScore = 0f

        idCardKeywords.forEach { keyword ->
            if (textLower.contains(keyword)) {
                matchCount++
                totalScore += when (keyword.length) {
                    in 8..20 -> 0.3f  // High value for specific terms
                    in 5..7 -> 0.2f   // Medium value
                    else -> 0.1f      // Lower value for short terms
                }
            }
        }

        // Check for date patterns (birth dates, expiry dates)
        val datePattern = Regex("""(\d{1,2}[/.-]\d{1,2}[/.-]\d{2,4})""")
        if (datePattern.containsMatchIn(text)) {
            totalScore += 0.2f
        }

        // Check for address patterns
        if (textLower.contains(Regex("""(\d+\s+[a-z\s]+)"""))) {
            totalScore += 0.1f
        }

        return minOf(totalScore, 1f)
    }

    private fun analyzeLabelsForIdCard(labels: List<Pair<String, Float>>): Float {
        val relevantLabels = listOf(
            "document", "paper", "card", "license", "identification",
            "certificate", "official", "government", "legal"
        )

        var maxConfidence = 0f
        labels.forEach { (label, confidence) ->
            if (relevantLabels.any { it in label.lowercase() }) {
                maxConfidence = maxOf(maxConfidence, confidence)
            }
        }

        return maxConfidence
    }
}

private suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { cameraProvider ->
        cameraProvider.addListener({
            continuation.resume(cameraProvider.get())
        }, ContextCompat.getMainExecutor(this))
    }
}

private fun capturePhoto(
    imageCapture: ImageCapture?,
    outputDirectory: File,
    executor: Executor,
    onPhotoCaptured: (File) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {
    val photoFile = File(
        outputDirectory,
        "ID_${System.currentTimeMillis()}.jpg"
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
private fun CameraPreview() {
    var result by remember { mutableStateOf<File?>(null) }
    var isAuto by remember { mutableStateOf(false) }
    AppTheme {
        Column {
            CameraView(Modifier.weight(1f).fillMaxSize()) { file, bool ->
                result = file
                isAuto = bool
            }
            Text(
                "Dernier fichier: ${result?.absolutePath ?: "Aucun"} (Auto: $isAuto)",
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