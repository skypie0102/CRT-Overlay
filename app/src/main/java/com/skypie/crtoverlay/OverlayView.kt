package com.skypie.crtoverlay

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class OverlayView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val scanPaint = Paint()
    private val vignettePaint = Paint()

    // tweak these to change the effect
    private val scanlineSpacing = 4           // pixels between lines
    private val scanlineHeight = 1            // line thickness
    private val scanAlpha = 60                // 0..255
    private val chromaOffset = 2f             // pixels

    init {
        paint.isFilterBitmap = true

        scanPaint.style = Paint.Style.FILL
        scanPaint.color = Color.argb(scanAlpha, 0, 0, 0)

        vignettePaint.shader = RadialGradient(
            width / 2f, height / 2f, Math.max(width, height) / 1.2f,
            intArrayOf(Color.TRANSPARENT, Color.argb(120, 0, 0, 0)),
            floatArrayOf(0.6f, 1f),
            Shader.TileMode.CLAMP
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // draw scanlines
        val h = height
        for (y in 0 until h step scanlineSpacing) {
            canvas.drawRect(0f, y.toFloat(), width.toFloat(), (y + scanlineHeight).toFloat(), scanPaint)
        }

        // chromatic offset: draw three thin color layers slightly shifted
        val save = canvas.saveLayer(null, null)
        // red
        paint.colorFilter = PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP)
        canvas.translate(-chromaOffset, 0f)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        canvas.restoreToCount(save)

        val save2 = canvas.saveLayer(null, null)
        paint.colorFilter = PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP)
        canvas.translate(0f, 0f)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        canvas.restoreToCount(save2)

        val save3 = canvas.saveLayer(null, null)
        paint.colorFilter = PorterDuffColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP)
        canvas.translate(chromaOffset, 0f)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        canvas.restoreToCount(save3)

        // vignette on top
        val vignette = RadialGradient(
            width / 2f, height / 2f, Math.max(width, height) / 1.2f,
            intArrayOf(Color.TRANSPARENT, Color.argb(140, 0, 0, 0)),
            floatArrayOf(0.6f, 1f),
            Shader.TileMode.CLAMP
        )
        vignettePaint.shader = vignette
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), vignettePaint)

        // invalidate to keep overlay active — keep lightweight
        postInvalidateDelayed(33)
    }
}
