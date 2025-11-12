package com.hakanemik.ortakakil.retrofit

import android.os.Build
import androidx.annotation.RequiresApi
import com.hakanemik.ortakakil.helper.TimeUtils
import com.hakanemik.ortakakil.repo.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Named


class TokenRefreshInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
    @Named("authApi") private val authApi: OrtakAkilDaoInterface
) : Interceptor {

    private val lock = java.util.concurrent.locks.ReentrantLock()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        // Refresh endpoint’i ise dokunma (sonsuz döngüyü kır)
        if (original.url().encodedPath().endsWith("/api/Auth/refresh-token")) {
            return chain.proceed(original)
        }
        if (tokenManager.shouldProactiveRefresh(60_000L)) {
            lock.lock()
            try {
                // Çifte kontrol (başkası yenilemiş olabilir)
                if (!tokenManager.isAccessExpired() && tokenManager.timeUntilAccessExpiryMs() > 60_000L) {
                    // Artık gerek yok
                } else if (!tokenManager.isRefreshExpired()) {
                    val refresh = tokenManager.getRefreshToken()
                    if (refresh != null) {
                        val fresh = runBlocking { authApi.refresh(mapOf("refreshToken" to refresh)) }.data
                        if (fresh != null) {
                            val accessExp = fresh.tokenExpiry
                            val refreshExp = fresh.refreshExpiry
                            val accessExpMs= TimeUtils.isoToMillis(accessExp)
                            val refreshExpMs = TimeUtils.isoToMillis(refreshExp)

                            tokenManager.saveTokens(
                                fresh.accessToken,
                                fresh.refreshToken ,
                                accessExpMs,
                                refreshExpMs
                            )
                        } else {
                            tokenManager.clearTokens()
                        }
                    } else {
                        tokenManager.clearTokens()
                    }
                } else {
                    tokenManager.clearTokens()
                }
            } finally {
                lock.unlock()
            }
        }
        val access = tokenManager.getToken()
        val req = if (access != null)
            original.newBuilder().header("Authorization", "Bearer $access").build()
        else original

        var resp = chain.proceed(req)
        if (resp.code() != 401) return resp

        resp.close()

        lock.lock()
        try {
            // Bu arada bir başkası yenilemiş olabilir
            tokenManager.getToken()?.let { latest ->
                if (!tokenManager.isAccessExpired()) {
                    val retried = original.newBuilder()
                        .header("Authorization", "Bearer $latest").build()
                    return chain.proceed(retried)
                }
            }


            if (tokenManager.isRefreshExpired()) {
                tokenManager.clearTokens()
                return chain.proceed(original) // 401 kalsın, UI login’e döner
            }

            // Sessiz refresh
            val refreshToken = tokenManager.getRefreshToken() ?: run {
                tokenManager.clearTokens()
                return chain.proceed(original)
            }

            val fresh = runBlocking {
                authApi.refresh(mapOf("refreshToken" to refreshToken))
            }.data ?: run {
                tokenManager.clearTokens()
                return chain.proceed(original)
            }

            // yeni expiry’lerle kaydet
            val accessExpMs = TimeUtils.isoToMillis(fresh.tokenExpiry)
            val refreshExpMs = TimeUtils.isoToMillis(fresh.refreshExpiry)



            tokenManager.saveTokens(
                fresh.accessToken,fresh.refreshToken,accessExpMs,refreshExpMs
            )

            val newReq = original.newBuilder()
                .header("Authorization", "Bearer ${fresh.accessToken}")
                .build()
            return chain.proceed(newReq)
        } catch (_: Exception) {
            tokenManager.clearTokens()
            return chain.proceed(original)
        } finally {
            lock.unlock()
        }
    }
}

