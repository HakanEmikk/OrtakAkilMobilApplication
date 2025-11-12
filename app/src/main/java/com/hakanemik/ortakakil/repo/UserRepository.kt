package com.hakanemik.ortakakil.repo

import com.hakanemik.ortakakil.data.UserStorage
import com.hakanemik.ortakakil.entity.LoginApiResponse
import com.hakanemik.ortakakil.retrofit.OrtakAkilDaoInterface
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userStorage: UserStorage,
    @Named("authApi") private val authApi: OrtakAkilDaoInterface
) {

    // User info with Flow
    fun getUserNameFlow(): Flow<String?> = userStorage.getUserNameFlow()
    fun getUserIdFlow(): Flow<String?> = userStorage.getUserIdFlow()
    fun getUserEmailFlow(): Flow<String?> = userStorage.getUserEmailFlow()

    // One-time reads
    suspend fun getUserName(): String? = userStorage.getUserName()
    suspend fun getUserId(): String? = userStorage.getUserId()
    suspend fun getUserEmail(): String? = userStorage.getUserEmail()
    suspend fun getRememberMe():Boolean = userStorage.getRememberMe()

    suspend fun saveRememberMe(rememberMe: Boolean = false){
        userStorage.saveRememberMe(rememberMe)
    }
    suspend fun saveUserInfo(userId: String, userName: String, email: String? = null) {
        userStorage.saveUserInfo(userId, userName, email)
    }

    suspend fun logout() {
        userStorage.clearAllData()
    }

    suspend fun refreshWithRefreshToken(refreshToken: String): LoginApiResponse {
        return authApi.refresh(mapOf("refreshToken" to refreshToken))
    }

    suspend fun isLoggedIn(): Boolean = userStorage.isLoggedIn()
}