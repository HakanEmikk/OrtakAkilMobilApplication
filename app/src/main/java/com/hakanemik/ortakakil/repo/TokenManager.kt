package com.hakanemik.ortakakil.repo

import android.util.Log
import com.hakanemik.ortakakil.data.UserStorage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    private val userStorage: UserStorage
) {

    fun saveTokens(accessToken: String, refreshToken: String, accessExp: Long, refreshExp: Long) {
        userStorage.saveToken(accessToken,accessExp)
        userStorage.saveRefreshToken(refreshToken,refreshExp)
    }

    fun getToken(): String? = userStorage.getToken()
    fun getRefreshToken(): String? = userStorage.getRefreshToken()
    fun timeUntilAccessExpiryMs(): Long = userStorage.getTokenExp() - System.currentTimeMillis()

    /** Access 1 dk’den az kaldıysa (veya zaten bitmişse) proaktif yenile */
    fun shouldProactiveRefresh(bufferMs: Long = 60_000L): Boolean {
        if (isRefreshExpired()) return false // zaten login'e yönlendireceğiz
        val t = timeUntilAccessExpiryMs()
        return t <= bufferMs
    }
    fun clearTokens() {
        userStorage.clearTokens()
    }
    fun isAccessExpired(): Boolean = userStorage.isExpired(userStorage.getTokenExp())
    fun isRefreshExpired(): Boolean = userStorage.isExpired(userStorage.getRefreshExp())


}