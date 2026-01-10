package com.hakanemik.ortakakil.di

import android.content.Context
import androidx.credentials.CredentialManager
import com.google.firebase.storage.FirebaseStorage
import com.hakanemik.ortakakil.R
import com.hakanemik.ortakakil.data.UserStorage
import com.hakanemik.ortakakil.helper.GoogleAuthHelper
import com.hakanemik.ortakakil.repo.TokenManager
import com.hakanemik.ortakakil.retrofit.OrtakAkilDaoInterface
import com.hakanemik.ortakakil.retrofit.TokenRefreshInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "http://10.0.2.2:5000"

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun provideCredentialManager(@ApplicationContext ctx: Context): CredentialManager =
        CredentialManager.create(ctx)

    @Provides
    @Singleton
    fun provideGoogleAuthHelper(
        @ApplicationContext context: Context,
        credentialManager: CredentialManager
    ):GoogleAuthHelper{
        val serverClientId = context.getString(R.string.google_server_client_id)
        return GoogleAuthHelper(credentialManager,serverClientId)
    }

    @Provides
    @Singleton
    fun provideUserStorage(@ApplicationContext ctx: Context) = UserStorage(ctx)

    @Provides
    @Singleton
    fun provideTokenManager(userStorage: UserStorage) = TokenManager(userStorage)

    // 1) AUTH için çıplak OkHttp (Authorization eklemeyecek, interceptor yok)
    @Provides
    @Singleton
    @Named("authOkHttp")
    fun provideAuthOkHttp(): OkHttpClient =
        OkHttpClient.Builder()
            .build()

    // 2) AUTH Retrofit (refresh endpoint’ine gidecek)
    @Provides
    @Singleton
    @Named("authRetrofit")
    fun provideAuthRetrofit(@Named("authOkHttp") ok: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(ok)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    @Named("authApi")
    fun provideAuthApi(@Named("authRetrofit") r: Retrofit): OrtakAkilDaoInterface =
        r.create(OrtakAkilDaoInterface::class.java)

    // 3) API OkHttp (refresh interceptor’lı)
    @Provides
    @Singleton
    @Named("apiOkHttp")
    fun provideApiOkHttp(
        tokenManager: TokenManager,
        @Named("authApi") authApi: OrtakAkilDaoInterface
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(TokenRefreshInterceptor(tokenManager, authApi))
            .build()

    // 4) API Retrofit (günlük istekler)
    @Provides
    @Singleton
    fun provideRetrofit(@Named("apiOkHttp") ok: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(ok)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideOrtakAkilDao(retrofit: Retrofit): OrtakAkilDaoInterface =
        retrofit.create(OrtakAkilDaoInterface::class.java)
}
