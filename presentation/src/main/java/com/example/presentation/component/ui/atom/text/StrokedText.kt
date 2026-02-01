package com.example.presentation.component.ui.atom.text

import android.graphics.Paint.Align
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit

@Composable
fun StrokedText(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color,
    strokeColor: Color,
    fontSize: TextUnit,
    fontWeight: FontWeight, // 현재 Canvas 방식에서는 직접 적용되지 않음
    textAlign: Align = Align.LEFT,
    strokeWidth: Float = 2f
) {
    val density = LocalDensity.current
    val scaledFontSize = with(density) { fontSize.toPx() }

    val textPaintStroke = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        style = android.graphics.Paint.Style.STROKE
        textSize = scaledFontSize
        this.strokeWidth = strokeWidth
        strokeMiter = 10f
        strokeJoin = android.graphics.Paint.Join.ROUND
        this.textAlign = textAlign
    }

    val textPaint = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        style = android.graphics.Paint.Style.FILL
        textSize = scaledFontSize
        this.textAlign = textAlign
    }

    Canvas(modifier = modifier) {
        val x = when (textAlign) {
            Align.CENTER -> size.width / 2
            Align.RIGHT -> size.width
            else -> 0f
        }
        val y = size.height / 2 + scaledFontSize / 2

        drawIntoCanvas {
            textPaintStroke.color = strokeColor.toArgb()
            textPaint.color = textColor.toArgb()

            it.nativeCanvas.drawText(text, x, y, textPaintStroke)
            it.nativeCanvas.drawText(text, x, y, textPaint)
        }
    }
}
