package com.application.kotkot.scan

import android.util.Log
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.text.TextBlock


class TextDetectorProcessor : Detector.Processor<TextBlock> {


    override fun receiveDetections(detections: Detector.Detections<TextBlock>) {
        val items = detections.detectedItems
        for (i in 0..items.size() - 1) {
            val item = items.valueAt(i)
            if (item != null && item!!.getValue() != null) {
                Log.d("Processor", "Text detected! " + item!!.getValue())
            }
        }
    }

    override fun release() { }
}