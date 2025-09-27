package com.hakanemik.ortakakil.retrofit

import com.hakanemik.ortakakil.entity.LoginApiResponse
import com.hakanemik.ortakakil.entity.LoginRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface OrtakAkilDaoInterface {
    @POST("/api/Auth/login")
    suspend  fun login(@Body loginRequest: LoginRequest): LoginApiResponse
}