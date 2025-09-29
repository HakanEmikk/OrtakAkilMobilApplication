package com.hakanemik.ortakakil.retrofit

import com.hakanemik.ortakakil.entity.LoginApiResponse
import com.hakanemik.ortakakil.entity.LoginRequest
import com.hakanemik.ortakakil.entity.RegisterApiResponse
import com.hakanemik.ortakakil.entity.RegisterRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface OrtakAkilDaoInterface {
    @POST("/api/Auth/login")
    suspend  fun login(@Body loginRequest: LoginRequest): LoginApiResponse
    @POST("/api/Auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): RegisterApiResponse
}