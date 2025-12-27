package com.hakanemik.ortakakil.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
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
    val REMEMBER_ME = booleanPreferencesKey("remember_me")
    val USER_PICTURE = stringPreferencesKey("user_picture")
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
    fun saveToken(token: String,expAtMs: Long) {
        securePrefs.edit().putString("access_token", token).putLong("access_token_exp", expAtMs).apply()
    }

    fun getToken(): String? = securePrefs.getString("access_token", null)
    fun getTokenExp(): Long =securePrefs.getLong("access_token_exp", -1)

    fun saveRefreshToken(refreshToken: String,expAtMs: Long) {
        securePrefs.edit().putString("refresh_token", refreshToken).putLong("refresh_token_exp", expAtMs).apply()
    }

    fun getRefreshToken(): String? = securePrefs.getString("refresh_token", null)
    fun getRefreshExp(): Long = securePrefs.getLong("refresh_token_exp", -1)

    fun clearTokens() {
        securePrefs.edit()
            .remove("access_token").remove("access_token_exp")
            .remove("refresh_token").remove("refresh_token_exp")
            .apply()
    }

    fun isExpired(expAtMs: Long, skewMs: Long = 60_000L): Boolean =
        expAtMs <= 0L || expAtMs <= System.currentTimeMillis() + skewMs
    // USER INFO WITH FLOW
    suspend fun saveUserInfo(userId: String, userName: String, email: String? = null,pictureUrl: String? = null) {
        context.dataStore.edit { prefs ->
            prefs[UserPrefs.USER_ID] = userId
            prefs[UserPrefs.USER_NAME] = userName
            email?.let { prefs[UserPrefs.USER_EMAIL] = it }
            pictureUrl?.let { prefs[UserPrefs.USER_PICTURE] = it }
        }
    }
    suspend fun  saveRememberMe(rememberMe: Boolean ){
        context.dataStore.edit { it[UserPrefs.REMEMBER_ME] = rememberMe }
    }


    fun getUserIdFlow(): Flow<String?> =
        context.dataStore.data.map { it[UserPrefs.USER_ID] }

    fun getUserNameFlow(): Flow<String?> =
        context.dataStore.data.map { it[UserPrefs.USER_NAME] }

    fun getUserEmailFlow(): Flow<String?> =
        context.dataStore.data.map { it[UserPrefs.USER_EMAIL] }
    fun getRememberMeFlow() : Flow<Boolean> =
        context.dataStore.data.map { it[UserPrefs.REMEMBER_ME] ?: false }
    fun getUserPictureFlow():Flow<String?> =
        context.dataStore.data.map { it[UserPrefs.USER_PICTURE] }
    suspend fun getUserId(): String? = getUserIdFlow().first()
    suspend fun getUserName(): String? = getUserNameFlow().first()
    suspend fun getUserEmail(): String? = getUserEmailFlow().first()
    suspend fun getUserPicture(): String? = getUserPictureFlow().first()
    suspend fun getRememberMe(): Boolean =getRememberMeFlow().first()

    suspend fun clearAllData() {
        clearTokens()
        context.dataStore.edit { it.clear() }
    }

    suspend fun isLoggedIn(): Boolean {
        return getToken() != null && getUserId() != null
    }
}