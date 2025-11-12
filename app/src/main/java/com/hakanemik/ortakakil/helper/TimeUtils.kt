package com.hakanemik.ortakakil.helper

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

object TimeUtils {
    /** ISO-8601 (…Z veya +03:00 gibi offsetli) string → epoch millis */
    @RequiresApi(Build.VERSION_CODES.O)
    fun isoToMillis(iso: String): Long {
        return try {
            // Örn: 2025-12-11T11:47:31.9122069Z (7 hane kesir desteklenir)
            Instant.parse(iso).toEpochMilli()
        } catch (_: Exception) {
            // Bazı backend’ler Z koymaz ama offset içerir: 2025-12-11T14:47:31.9122069+03:00
            OffsetDateTime.parse(iso, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                .toInstant()
                .toEpochMilli()
        }
    }

    /** expiresIn saniye gelirse millis’e çevir */
    fun expiresInSecsToMillis(expiresInSeconds: Long): Long =
        System.currentTimeMillis() + expiresInSeconds * 1000
}
