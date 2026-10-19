package com.example.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint

object ImagePreprocessor {

    /**
     * Preprocesses a Kitab Kuning manuscript or document image for optimal OCR:
     * - Grayscale conversion
     * - Contrast enhancement to separate ancient faded ink from parchment paper
     * - Slight brightness adjustment
     */
    fun preprocessForOcr(
        source: Bitmap,
        applyGrayscale: Boolean = true,
        contrast: Float = 1.6f, // >1 increases contrast
        brightness: Float = -10f
    ): Bitmap {
        val width = source.width
        val height = source.height
        val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        val cm = ColorMatrix()

        if (applyGrayscale) {
            cm.setSaturation(0f)
        }

        if (contrast != 1f || brightness != 0f) {
            // Contrast matrix: scale = contrast, translate = (1 - scale) / 2 * 255 + brightness
            val scale = contrast
            val translate = (1f - scale) * 128f + brightness
            val contrastMatrix = ColorMatrix(
                floatArrayOf(
                    scale, 0f, 0f, 0f, translate,
                    0f, scale, 0f, 0f, translate,
                    0f, 0f, scale, 0f, translate,
                    0f, 0f, 0f, 1f, 0f
                )
            )
            cm.postConcat(contrastMatrix)
        }

        paint.colorFilter = ColorMatrixColorFilter(cm)
        canvas.drawBitmap(source, 0f, 0f, paint)
        return output
    }

    /**
     * Binarizes an image (threshold black/white) to eliminate yellow parchment noise.
     */
    fun binarize(source: Bitmap, threshold: Int = 135): Bitmap {
        val width = source.width
        val height = source.height
        val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val pixels = IntArray(width * height)
        source.getPixels(pixels, 0, width, 0, 0, width, height)

        for (i in pixels.indices) {
            val color = pixels[i]
            val r = (color shr 16) and 0xFF
            val g = (color shr 8) and 0xFF
            val b = color and 0xFF
            // Luminance formula
            val gray = (0.299 * r + 0.587 * g + 0.114 * b).toInt()
            val finalColor = if (gray < threshold) 0xFF000000.toInt() else 0xFFFFFFFF.toInt()
            pixels[i] = finalColor
        }

        output.setPixels(pixels, 0, width, 0, 0, width, height)
        return output
    }
}
