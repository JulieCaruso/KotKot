package com.application.kotkot.photo

import android.content.Context
import android.content.Context.*
import android.graphics.Point
import android.hardware.Camera
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.SurfaceHolder
import com.application.kotkot.R
import com.application.kotkot.utils.OpenAlprUtils
import kotlinx.android.synthetic.main.activity_photo.*
import android.support.v4.view.ViewCompat.getRotation
import android.view.WindowManager
import android.view.Display
import android.view.Surface


class PhotoActivity : AppCompatActivity() {

    var camera: Camera? = null
    var inPreview = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)

        preview.holder.addCallback(callback)
        val size = Point()
        windowManager.defaultDisplay.getSize(size)
        preview.holder.setFixedSize(size.x, size.y)
        capture.setOnClickListener({
            camera?.takePicture(null, null, { bytes: ByteArray, camera: Camera -> onTakePicture(bytes) })
        })
    }

    override fun onResume() {
        super.onResume()
        camera = Camera.open()
    }

    override fun onPause() {
        super.onPause()
        if (inPreview) camera?.stopPreview()
        camera?.release()
        camera = null
        inPreview = false
    }

    fun onTakePicture(bytes: ByteArray) {
        val imagePath = OpenAlprUtils.writeFile(bytes)
        Log.d("azerty", "openalpr ${OpenAlprUtils.create(this, imagePath)}")
    }

    val callback = object : SurfaceHolder.Callback {
        override fun surfaceCreated(holder: SurfaceHolder?) {
            try {
                camera?.setPreviewDisplay(preview.holder)
            } catch (e: Throwable) {
                Log.e("azerty", e.message)
            }
        }

        override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
            val params = camera?.parameters

            var size: Camera.Size? = null
            val display = (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
            if (display.rotation == Surface.ROTATION_0) {
                size = getBestPreviewSize(height, width, params)
                camera?.setDisplayOrientation(90)
            }
            if (display.rotation == Surface.ROTATION_90) {
                size = getBestPreviewSize(width, height, params)
            }
            if (display.rotation == Surface.ROTATION_180) {
                size = getBestPreviewSize(height, width, params)
            }
            if (display.rotation == Surface.ROTATION_270) {
                size = getBestPreviewSize(width, height, params)
                camera?.setDisplayOrientation(180)
            }

            if (size != null) {
                params?.setPreviewSize(size.width, size.height)
                camera?.parameters = params
                camera?.startPreview()
                inPreview = true
            }
        }

        override fun surfaceDestroyed(holder: SurfaceHolder?) {}
    }

    fun getBestPreviewSize(width: Int, height: Int, parameters: Camera.Parameters?): Camera.Size? {
        var result: Camera.Size? = null
        if (parameters != null) {
            for (size in parameters.supportedPreviewSizes) {
                if (size.width <= width && size.height <= height) {
                    if (result == null) {
                        result = size
                    } else {
                        val resultArea = result.width * result.height
                        val newArea = size.width * size.height
                        if (newArea > resultArea) {
                            result = size
                        }
                    }
                }
            }
        }
        return result
    }
}
