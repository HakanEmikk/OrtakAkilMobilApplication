package com.hakanemik.ortakakil.di

import android.content.Context
import com.hakanemik.ortakakil.data.UserStorage
import com.hakanemik.ortakakil.retrofit.ApiUtils
import com.hakanemik.ortakakil.retrofit.OrtakAkilDaoInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOrtakAkilDao(): OrtakAkilDaoInterface =
        ApiUtils.getOrtakAkilDaoInterface()

    @Provides
    @Singleton
    fun provideUserStorage(@ApplicationContext context: Context): UserStorage =
        UserStorage(context)
}
