package com.hakanemik.ortakakil.helper

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

object FileHelper {
    fun createImageUri(context: Context
    ): Uri {
        val file = File(context.cacheDir, "camera_photos").apply { if (!exists()) mkdirs() }
        val photoFile = File(file, "temp_profile.jpg")
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            photoFile
        )
    }
}