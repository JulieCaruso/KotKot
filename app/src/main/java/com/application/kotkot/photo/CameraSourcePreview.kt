package com.application.kotkot.photo

import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewGroup
import com.google.android.gms.vision.CameraSource
import java.io.IOException

class CameraSourcePreview : ViewGroup {
    val TAG = "CameraSourcePreview"

    val surfaceView: SurfaceView = SurfaceView(context)

    var cameraSource: CameraSource? = null
    var startRequested = false
    var surfaceAvailable = false

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        surfaceView.holder.addCallback(SurfaceCallback())
        addView(surfaceView)
    }

    @Throws(IOException::class)
    fun start(cameraSource: CameraSource) {
        if (cameraSource == null)
            stop()

        this.cameraSource = cameraSource

        if (cameraSource != null) {
            startRequested = true
            startIfReady()
        }
    }

    fun stop() = cameraSource?.stop()

    fun release() {
        cameraSource?.release()
        cameraSource = null
    }

    @Throws(IOException::class)
    fun startIfReady() {
        if (startRequested && surfaceAvailable && cameraSource != null) {
            cameraSource?.start(surfaceView.getHolder())
            startRequested = false
        }
    }

    inner class SurfaceCallback : SurfaceHolder.Callback {
        override fun surfaceCreated(surface: SurfaceHolder) {
            surfaceAvailable = true
            try {
                startIfReady()
            } catch (e: IOException) {
                Log.e(TAG, "Could not start camera source.", e)
            }
        }

        override fun surfaceDestroyed(surface: SurfaceHolder) {
            surfaceAvailable = false
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var width = 320
        var height = 240
        if (cameraSource != null) {
            val size = cameraSource?.getPreviewSize()
            if (size != null) {
                width = size.getWidth()
                height = size.getHeight()
            }
        }

        // Swap width and height sizes when in portrait, since it will be rotated 90 degrees
        if (isPortraitMode()) {
            val tmp = width
            width = height
            height = tmp
        }

        val layoutWidth = right - left
        val layoutHeight = bottom - top
        val cameraRatio = width.toFloat() / height.toFloat()
        val screenRatio = layoutWidth.toFloat() / layoutHeight.toFloat()

        // Resize CameraSource keeping its ratio to fit in the screen
        val childWidth: Int
        val childHeight: Int
        if (screenRatio < cameraRatio) {
            childWidth = (layoutHeight.toFloat() * cameraRatio).toInt()
            childHeight = layoutHeight
        } else {
            childWidth = layoutWidth
            childHeight = (layoutWidth.toFloat() / cameraRatio).toInt()
        }

        for (i in 0..childCount - 1) {
            getChildAt(i).layout(0, 0, childWidth, childHeight)
        }

        try {
            startIfReady()
        } catch (e: IOException) {
            Log.e(TAG, "Could not start camera source.", e)
        }

    }

    private fun isPortraitMode(): Boolean {
        val orientation = context.resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return false
        }
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            return true
        }

        Log.d(TAG, "isPortraitMode returning false by default")
        return false
    }

}
