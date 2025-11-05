package com.hakanemik.ortakakil.repo

import com.hakanemik.ortakakil.data.UserStorage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userStorage: UserStorage
) {
    // Token operations
    fun saveToken(token: String) = userStorage.saveToken(token)
    fun getToken(): String? = userStorage.getToken()
    fun clearToken() = userStorage.clearToken()

    // User info with Flow
    fun getUserNameFlow(): Flow<String?> = userStorage.getUserNameFlow()
    fun getUserIdFlow(): Flow<String?> = userStorage.getUserIdFlow()
    fun getUserEmailFlow(): Flow<String?> = userStorage.getUserEmailFlow()

    // One-time reads
    suspend fun getUserName(): String? = userStorage.getUserName()
    suspend fun getUserId(): String? = userStorage.getUserId()
    suspend fun getUserEmail(): String? = userStorage.getUserEmail()

    suspend fun saveUserInfo(userId: String, userName: String, email: String? = null) {
        userStorage.saveUserInfo(userId, userName, email)
    }

    suspend fun logout() {
        userStorage.clearAllData()
    }

    suspend fun isLoggedIn(): Boolean = userStorage.isLoggedIn()
}