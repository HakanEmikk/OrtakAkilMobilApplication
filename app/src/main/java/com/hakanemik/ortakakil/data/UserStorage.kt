package com.hakanemik.ortakakil.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore by preferencesDataStore("user_prefs")

object UserPrefs {
    val USER_ID = stringPreferencesKey("user_id")
    val USER_NAME = stringPreferencesKey("user_name")
    val USER_EMAIL = stringPreferencesKey("user_email")
}

@Singleton
class UserStorage @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val securePrefs = EncryptedSharedPreferences.create(
        context,
        "secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    // TOKEN OPERATIONS
    fun saveToken(token: String) {
        securePrefs.edit().putString("access_token", token).apply()
    }

    fun getToken(): String? = securePrefs.getString("access_token", null)

    fun clearToken() {
        securePrefs.edit().remove("access_token").apply()
    }

    // USER INFO WITH FLOW
    suspend fun saveUserInfo(userId: String, userName: String, email: String? = null) {
        context.dataStore.edit { prefs ->
            prefs[UserPrefs.USER_ID] = userId
            prefs[UserPrefs.USER_NAME] = userName
            email?.let { prefs[UserPrefs.USER_EMAIL] = it }
        }
    }

    fun getUserIdFlow(): Flow<String?> =
        context.dataStore.data.map { it[UserPrefs.USER_ID] }

    fun getUserNameFlow(): Flow<String?> =
        context.dataStore.data.map { it[UserPrefs.USER_NAME] }

    fun getUserEmailFlow(): Flow<String?> =
        context.dataStore.data.map { it[UserPrefs.USER_EMAIL] }

    suspend fun getUserId(): String? = getUserIdFlow().first()
    suspend fun getUserName(): String? = getUserNameFlow().first()
    suspend fun getUserEmail(): String? = getUserEmailFlow().first()

    suspend fun clearAllData() {
        clearToken()
        context.dataStore.edit { it.clear() }
    }

    suspend fun isLoggedIn(): Boolean {
        return getToken() != null && getUserId() != null
    }
}