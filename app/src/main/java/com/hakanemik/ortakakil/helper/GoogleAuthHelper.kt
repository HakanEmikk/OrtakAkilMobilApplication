package com.hakanemik.ortakakil.helper

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential


class GoogleAuthHelper(
    private val credentialManager: CredentialManager,
    private val serverClientId: String
) {

    suspend fun getIdToken(context: Context):String{
        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(serverClientId)
            .setFilterByAuthorizedAccounts(false)
            .setAutoSelectEnabled(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val activityContext = context.findActivity()
            ?: throw IllegalStateException("Google Giriş için Activity Context gereklidir.")

        val result = credentialManager.getCredential(activityContext, request)
        val googleCred = GoogleIdTokenCredential.createFrom(result.credential.data)
        return  googleCred.idToken
    }
    private fun Context.findActivity(): android.app.Activity? {
        var context = this
        while (context is android.content.ContextWrapper) {
            if (context is android.app.Activity) return context
            context = context.baseContext
        }
        return null
    }
}