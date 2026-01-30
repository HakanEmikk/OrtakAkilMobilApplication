package com.hakanemik.ortakakil.retrofit

import com.hakanemik.ortakakil.entity.AiRequest
import com.hakanemik.ortakakil.entity.AiResponse
import com.hakanemik.ortakakil.entity.ApiResponse
import com.hakanemik.ortakakil.entity.BlockedUserResponse
import com.hakanemik.ortakakil.entity.CommentRequest
import com.hakanemik.ortakakil.entity.CommentResponse
import com.hakanemik.ortakakil.entity.DiscoveryResponse
import com.hakanemik.ortakakil.entity.HistoryResponse
import com.hakanemik.ortakakil.entity.LoginRequest
import com.hakanemik.ortakakil.entity.LoginResponse
import com.hakanemik.ortakakil.entity.RegisterRequest
import com.hakanemik.ortakakil.entity.ReportRequest
import com.hakanemik.ortakakil.entity.ShareRequest
import com.hakanemik.ortakakil.entity.User
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface OrtakAkilDaoInterface {
    @POST("/api/Auth/login")
    suspend  fun login(@Body loginRequest: LoginRequest): ApiResponse<LoginResponse>
    @POST("/api/Auth/google-login")
    suspend fun googleWithLogin(@Body body: Map<String,String>): ApiResponse<LoginResponse>
    @POST("/api/Auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): ApiResponse<User>
    @POST("/api/AI/ask")
    suspend fun aiRequest(@Body aiRequest: AiRequest):ApiResponse<AiResponse>
    @POST("/api/Auth/refresh-token")
    suspend fun refresh(@Body body: Map<String,String>):ApiResponse<LoginResponse>
    @GET("/api/User/GetProfile")
    suspend fun loadProfile():ApiResponse<User>
    @PUT("/api/User/UpdateProfile")
    suspend fun updateProfile(@Body user: User):ApiResponse<User>
    @DELETE("/api/User/DeleteUser{id}")
    suspend fun deleteUser(@Path("id") userId: Int): ApiResponse<Boolean>
    @POST("/api/Discover/share")
    suspend fun shareAnswer(@Body shareRequest : ShareRequest) : ApiResponse<Boolean>
    @POST("/api/Discover/share")
    suspend fun unshareAnswer(@Body shareRequest : ShareRequest): ApiResponse<Boolean>
    @GET("/api/Discover/feed")
    suspend fun loadFeed( @Query("page") page: Int):ApiResponse<List<DiscoveryResponse>>
    @POST("/api/Discover/like/{id}")
    suspend fun likeDecision(@Path("id") id: Int): ApiResponse<Boolean>
    @POST("/api/Report/create")
    suspend fun reportAnswer(@Body reportRequest: ReportRequest) : ApiResponse<Boolean>
    @POST("/api/UserBlock/block/{blockedId}")
    suspend fun blockUser(@Path("blockedId") blockedId: Int) : ApiResponse<Boolean>
    @GET("/api/UserBlock/blocked-users")
    suspend fun getBlockedUsers() : ApiResponse<List<BlockedUserResponse>>
    @POST("/api/UserBlock/unblock/{blockedId}")
    suspend fun unblockUser(@Path("blockedId") blockedId: Int) : ApiResponse<Boolean>
    @POST("/api/Discover/add-comment")
    suspend fun addComment(@Body commentRequest: CommentRequest): ApiResponse<Boolean>
    @GET("/api/Discover/get-comments/{decisionId}")
    suspend fun getComments(@Path("decisionId") decisionId: Int): ApiResponse<List<CommentResponse>>
    @GET("/api/Discover/get-history")
    suspend fun getHistory(@Query("pageNumber") pageNumber: Int): ApiResponse<List<HistoryResponse>>
    @POST("/api/Auth/logout")
    suspend fun logout(@Body body: Map<String,String>):ApiResponse<Boolean>
}