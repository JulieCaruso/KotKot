package com.application.kotkot.scan

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.application.kotkot.R
import com.application.kotkot.utils.OpenAlprUtils
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.text.TextRecognizer
import kotlinx.android.synthetic.main.activity_scan.*
import java.io.IOException


class ScanActivity : AppCompatActivity() {
    companion object {
        val EXTRA_TYPE_CAPTURE = "EXTRA_TYPE_CAPTURE"
        val TYPE_VISION = 1
        val TYPE_CAPTURE = 2
        val TYPE_OPEN_ALPR = 3
    }

    val TAG = "ScanActivity"
    val REQUEST_PERMISSION_CAMERA = 10

    var type = TYPE_VISION
    var cameraSource: CameraSource? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        type = intent?.getIntExtra(EXTRA_TYPE_CAPTURE, TYPE_VISION) ?: TYPE_VISION

        capture.setOnClickListener {
            cameraSource?.takePicture(
                    {},
                    {image ->
                        val file = OpenAlprUtils.writeFile(image)
                        Log.d("azerty", OpenAlprUtils.create(this, file))
                    })
        }

        /*if (type == TYPE_VISION) {
            capture.visibility = View.GONE
        }*/
    }

    override fun onResume() {
        super.onResume()

        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            createCameraSource()
        } else {
            requestCameraPermission()
        }
        startCameraSource()
    }

    override fun onPause() {
        super.onPause()
        preview.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        preview.release()
    }

    fun createCameraSource() {
        val textRecognizer = TextRecognizer.Builder(this)
                .build()
        textRecognizer.setProcessor(TextDetectorProcessor())

        if (!textRecognizer.isOperational) {
            Log.d(TAG, "Detector dependencies are not yet available.")

            // Check for low storage.  If there is low storage, the native library will not be
            // downloaded, so detection will not become operational.
            val lowstorageFilter = IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW)
            val hasLowStorage = registerReceiver(null, lowstorageFilter) != null

            if (hasLowStorage) {
                Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show()
                Log.d(TAG, getString(R.string.low_storage_error))
            }
        }

        cameraSource = CameraSource.Builder(this, textRecognizer)
                .setRequestedPreviewSize(640, 480)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(30.0f)
                .setAutoFocusEnabled(true)
                .build()
    }

    fun startCameraSource() {
        if (cameraSource != null) {
            try {
                preview.start(cameraSource!!)
            } catch (e: IOException) {
                Log.e(TAG, "Unable to start camera source.", e)
                cameraSource?.release()
                cameraSource = null
            }

        }
    }

    fun requestCameraPermission() {
        val permissions = arrayOf(Manifest.permission.CAMERA)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                requestPermissions(permissions, REQUEST_PERMISSION_CAMERA)
            } else {
                Snackbar.make(layout, R.string.permission_camera_details, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.ok, { requestPermissions(permissions, REQUEST_PERMISSION_CAMERA) })
                        .show()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSION_CAMERA) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createCameraSource()
            } else {
                finish()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}
