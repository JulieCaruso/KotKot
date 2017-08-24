package com.application.kotkot.utils.ui

import android.content.Context
import android.content.res.Configuration

class UiUtils {
    companion object {
        fun isPortraitMode(context: Context): Boolean {
            val orientation = context.resources.configuration.orientation
            if (orientation == Configuration.ORIENTATION_LANDSCAPE)
                return false
            if (orientation == Configuration.ORIENTATION_PORTRAIT)
                return true
            return false
        }
    }
}