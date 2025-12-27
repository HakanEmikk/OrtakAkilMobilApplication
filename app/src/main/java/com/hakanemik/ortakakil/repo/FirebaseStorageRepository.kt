package com.hakanemik.ortakakil.repo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storageMetadata
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseStorageRepository @Inject constructor(
    private val storage: FirebaseStorage,
    @ApplicationContext private val context: Context
) {
    suspend fun uploadProfilePhotoAndGetUrl(
        userId: String,
        photoUri: Uri
    ): String = withContext(Dispatchers.IO) {
        val bytes = compressImage(photoUri)
        val fileName = "profile_${UUID.randomUUID()}.jpg"
        val metadata = storageMetadata {
            contentType = "image/jpeg"
        }
        val ref = storage.reference
            .child("photos")
            .child(userId)
            .child("profile")
            .child(fileName)

        ref.putBytes(bytes,metadata).await()
        return@withContext ref.downloadUrl.await().toString()
    }
    private fun compressImage(photoUri: Uri): ByteArray {
        val bytes = context.contentResolver.openInputStream(photoUri)?.use { inputStream ->
            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            val outputStream = ByteArrayOutputStream()
            originalBitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
            outputStream.toByteArray()
        }
        return bytes ?: throw IOException("Görüntü okunamadı")
    }
    suspend fun deletePhoto(photoUrl: String){
        try {
            storage.getReferenceFromUrl(photoUrl).delete().await()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}
