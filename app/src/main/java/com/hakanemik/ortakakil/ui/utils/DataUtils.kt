package com.hakanemik.ortakakil.ui.utils

import android.os.Build

object DateUtils {

    fun calculateTimeAgo(dateString: String?): String {
        if (dateString.isNullOrEmpty()) return "Bilinmiyor"

        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val pastMillis = com.hakanemik.ortakakil.helper.TimeUtils.isoToMillis(dateString)
                val nowMillis = System.currentTimeMillis()

                val diffInSeconds = (nowMillis - pastMillis) / 1000

                // Gelecek tarih kontrolü (Server saati biraz ilerideyse)
                if (diffInSeconds < 0) return "Az önce"

                val minutes = diffInSeconds / 60
                val hours = minutes / 60
                val days = hours / 24

                when {
                    minutes < 1 -> "Az önce"
                    minutes < 60 -> "$minutes dakika önce"
                    hours < 24 -> "$hours saat önce"
                    days == 1L -> "Dün"
                    days < 30 -> "$days gün önce"
                    days < 365 -> "${days / 30} ay önce"
                    else -> "${days / 365} yıl önce"
                }
            } else {
                dateString.substringBefore("T")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback: Hata olursa sadece tarihi göster
            dateString.split("T").firstOrNull() ?: "Bilinmiyor"
        }
    }
}
