package com.github.rtyvZ.kitties.ui.customViews

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.toRectF

class ImageWithStateUpload(context: Context, attributeSet: AttributeSet) :
    AppCompatImageView(context, attributeSet) {

    private val paintCircleImage = Paint(Paint.ANTI_ALIAS_FLAG)
    private val imageRect = Rect()
    private val point = Point()
    private lateinit var maskBitmap: Bitmap
    private var resultBitmap: Bitmap? = null
    private lateinit var srcBitmap: Bitmap
    private val imageOffset = 20
    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var centerX: Int = 0
    private var centerY: Int = 0
    private var progressRect = RectF()
    private var sweepAngle = 0f

    init {
        (context as Activity).display?.getRealSize(point)

        with(paintCircleImage) {
            style = Paint.Style.FILL
        }

        with(progressPaint) {
            style = Paint.Style.STROKE
            color = Color.RED
            strokeWidth = 5f
        }
    }

    fun setProgress(progress: Int) {
        val angle = (progress.toFloat() / 100) * 360
        sweepAngle = angle
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = resolveDefaultSize(widthMeasureSpec)
        setMeasuredDimension(size, size)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if (w == 0) return
        with(imageRect) {
            left = 0 + imageOffset
            top = 0 + imageOffset
            right = w - imageOffset
            bottom = h - imageOffset
        }
        //  prepareBitmap(w, h)
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        prepareBitmap(width, height)
        centerX = width / 2
        centerY = height / 2
        val radius = Math.min(width, height) / 2 - imageOffset / 2
        progressRect.set(
            centerX - radius.toFloat(),
            centerY - radius.toFloat(),
            centerX + radius.toFloat(),
            centerY + radius.toFloat()
        )
    }

    private fun resolveDefaultSize(spec: Int): Int {
        return when (MeasureSpec.getMode(spec)) {
            MeasureSpec.UNSPECIFIED -> {
                getUnspecifiedSizeImage()
            }
            MeasureSpec.AT_MOST -> {
                MeasureSpec.getSize(spec)
            }
            MeasureSpec.EXACTLY -> {
                MeasureSpec.getSize(spec)
            }
            else -> MeasureSpec.getSize(spec)
        }
    }

    private fun getUnspecifiedSizeImage(): Int {
        return point.x.coerceAtMost(point.y)
    }

    private fun prepareBitmap(w: Int, h: Int) {
        maskBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ALPHA_8)

        val maskCanvas = Canvas(maskBitmap)
        maskCanvas.drawOval(imageRect.toRectF(), paintCircleImage)
        paintCircleImage.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        resultBitmap = maskBitmap.copy(Bitmap.Config.ARGB_8888, true)

        drawable?.let {
            srcBitmap = it.toBitmap(w, h, Bitmap.Config.ARGB_8888)
            resultBitmap?.let { bitmap ->
                val resultCanvas = Canvas(bitmap)
                resultCanvas.drawBitmap(maskBitmap, imageRect, imageRect, null)
                resultCanvas.drawBitmap(srcBitmap, imageRect, imageRect, paintCircleImage)
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        resultBitmap?.let {
            canvas?.drawBitmap(it, imageRect, imageRect, null)
        }
        canvas?.drawArc(progressRect, -90f, sweepAngle, false, progressPaint)
        invalidate()
    }
}