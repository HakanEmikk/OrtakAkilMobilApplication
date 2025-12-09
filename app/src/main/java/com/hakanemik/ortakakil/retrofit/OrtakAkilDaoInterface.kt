package com.hakanemik.ortakakil.retrofit

import com.hakanemik.ortakakil.entity.AiRequest
import com.hakanemik.ortakakil.entity.AiResponse
import com.hakanemik.ortakakil.entity.ApiResponse
import com.hakanemik.ortakakil.entity.LoginRequest
import com.hakanemik.ortakakil.entity.LoginResponse
import com.hakanemik.ortakakil.entity.RegisterRequest
import com.hakanemik.ortakakil.entity.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface OrtakAkilDaoInterface {
    @POST("/api/Auth/login")
    suspend  fun login(@Body loginRequest: LoginRequest): ApiResponse<LoginResponse>
    @POST("/api/Auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): ApiResponse<User>
    @POST("/api/AI/ask")
    suspend fun aiRequest(@Body aiRequest: AiRequest):ApiResponse<AiResponse>
    @POST("/api/Auth/refresh-token")
    suspend fun refresh(@Body body: Map<String,String>):ApiResponse<LoginResponse>
    @GET("/api/User/GetProfile")
    suspend fun loadProfile():ApiResponse<User>
}