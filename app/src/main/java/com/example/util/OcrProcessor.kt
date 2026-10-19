package com.example.util

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class OcrProcessor {
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    suspend fun recognizeText(
        sourceBitmap: Bitmap,
        applyGrayscale: Boolean = true,
        contrastMultiplier: Float = 1.6f
    ): OcrResult = withContext(Dispatchers.Default) {
        // Step 1: Preprocess image with grayscale and contrast boost for Kitab Kuning manuscript
        val preprocessed = ImagePreprocessor.preprocessForOcr(
            source = sourceBitmap,
            applyGrayscale = applyGrayscale,
            contrast = contrastMultiplier
        )

        // Step 2: Run ML Kit Text Recognition
        val inputImage = InputImage.fromBitmap(preprocessed, 0)

        suspendCancellableCoroutine { continuation ->
            recognizer.process(inputImage)
                .addOnSuccessListener { visionText ->
                    val rawText = visionText.text.trim()
                    continuation.resume(
                        OcrResult(
                            extractedText = rawText,
                            preprocessedBitmap = preprocessed,
                            blockCount = visionText.textBlocks.size
                        )
                    )
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
    }
}

data class OcrResult(
    val extractedText: String,
    val preprocessedBitmap: Bitmap,
    val blockCount: Int
)
