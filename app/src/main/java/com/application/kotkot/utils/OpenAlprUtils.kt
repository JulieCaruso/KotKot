package com.application.kotkot.utils

import android.content.Context
import android.os.Environment
import org.openalpr.OpenALPR
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class OpenAlprUtils {

    companion object {
        val ANDROID_DATA_DIR = "/data/data/com.application.kotkot"

        fun getConfFile(): String =
                ANDROID_DATA_DIR + File.separatorChar + "runtime_data" + File.separatorChar + "openalpr.conf"

        /**
         * @param context
         * @param imagePath absolute path of image containing the license plate
         */
        fun create(context: Context, imagePath: String): String =
                OpenALPR.Factory.create(context, ANDROID_DATA_DIR).recognizeWithCountryRegionNConfig("eu", "", imagePath, getConfFile(), 10)

        fun writeFile(image: ByteArray): String {
            // Use a folder to store all results
            val folder = File("${Environment.getExternalStorageDirectory()}/OpenALPR/")
            if (!folder.exists())
                folder.mkdir()

            // Generate the path for the next photo
            val df = SimpleDateFormat("yyyy-MM-dd-hh-mm-ss", Locale.getDefault())
            val name = df.format(Date())
            val destination = File(folder, "kotkot$name.jpg")
            if (!destination.exists())
                destination.createNewFile()

            val bos = BufferedOutputStream(FileOutputStream(destination))
            bos.write(image)
            bos.flush()
            bos.close()

            return destination.absolutePath
        }
    }

}