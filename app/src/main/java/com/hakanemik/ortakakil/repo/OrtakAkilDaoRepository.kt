package com.hakanemik.ortakakil.repo

import com.google.gson.Gson
import com.hakanemik.ortakakil.entity.AiRequest
import com.hakanemik.ortakakil.entity.AiResponse
import com.hakanemik.ortakakil.entity.ApiResponse
import com.hakanemik.ortakakil.entity.ErrorResponse
import com.hakanemik.ortakakil.entity.LoginRequest
import com.hakanemik.ortakakil.entity.LoginResponse
import com.hakanemik.ortakakil.entity.RegisterRequest
import com.hakanemik.ortakakil.entity.Resource
import com.hakanemik.ortakakil.entity.User
import com.hakanemik.ortakakil.retrofit.OrtakAkilDaoInterface
import javax.inject.Inject
import javax.inject.Named

class OrtakAkilDaoRepository @Inject constructor(
    private val ortakAkilDaoInterface: OrtakAkilDaoInterface,
    @Named("authApi") private val authApi: OrtakAkilDaoInterface,
){
    suspend  fun login(loginRequest: LoginRequest): Resource<ApiResponse<LoginResponse>> {
        return try {
            val response=authApi.login(loginRequest)
            Resource.Success(response)
        }catch (e: retrofit2.HttpException){
            val errorBody=e.response()?.errorBody()?.string()
            val backendMessage= try {
                Gson().fromJson(errorBody,ErrorResponse::class.java).message
            }catch (ex:Exception){
                null
            }
            val userFriendlyMessage = when (backendMessage) {
                "Email or password is incorrect." -> "E-posta veya şifre hatalı"
                "This account uses Google sign-in. Please login with Google." -> "Bu hesap Google oturum açma özelliğini kullanır. Lütfen Google ile giriş yapın."
                else -> "Bir hata oluştu, lütfen tekrar deneyin"
            }
            Resource.Error(userFriendlyMessage, e.code())
        } catch (e: java.net.UnknownHostException) {
            Resource.Error("İnternet bağlantınızı kontrol edin")
        }
        catch (e:Exception){
            Resource.Error("Beklenmeyen hata: ${e.localizedMessage}")
        }
    }
    suspend fun googleWithLogin(idToken:String): Resource<ApiResponse<LoginResponse>> {
        return try {
            val response=authApi.googleWithLogin(mapOf("idToken" to idToken))
            Resource.Success(response)
        }catch (e: retrofit2.HttpException){
            val errorBody=e.response()?.errorBody()?.string()
            val backendMessage= try {
                Gson().fromJson(errorBody,ErrorResponse::class.java).message
            }catch (ex:Exception){
                null
            }
            val userFriendlyMessage = when (backendMessage) {
                "Email or password is incorrect." -> "E-posta veya şifre hatalı"
                "Invalid Google token." -> "Geçersiz Google tokeni"
                "Google e-postası doğrulanmadı" -> "Google e-postası doğrulanmadı"
                "Google token payload missing required fields." -> "Google token payload eksik"
                "This email is registered with local login. Please login with email & password." -> "Bu e-posta bir yerel giriş ile kaydedildi. Lütfen e-posta ve şifre ile giriş yapın."
                "Account mismatch. Please contact support." -> "Hesap uyuşmazlığı. Lütfen destek ile iletişime geçin."
                "User inactive." -> "Kullanıcı pasif."
                else -> "Bir hata oluştu, lütfen tekrar deneyin"
            }
            Resource.Error(userFriendlyMessage, e.code())
        } catch (e: java.net.UnknownHostException) {
            Resource.Error("İnternet bağlantınızı kontrol edin")
        }
        catch (e:Exception){
            Resource.Error("Beklenmeyen hata: ${e.localizedMessage}")
        }
    }
    suspend fun register(registerRequest: RegisterRequest):Resource<ApiResponse<User>>{
        return try {
            val response=authApi.register(registerRequest)
            Resource.Success(response)
        }catch (e: retrofit2.HttpException){
            val errorBody=e.response()?.errorBody()?.string()
            val backendMessage= try {
                Gson().fromJson(errorBody,ErrorResponse::class.java).message
            }catch (ex:Exception){
                null
            }
            val userFriendlyMessage = when (backendMessage) {
                "This email address is already in use." -> "Bu e-posta adresi zaten kullanılıyor."
                else -> "Bir hata oluştu, lütfen tekrar deneyin"
            }
            Resource.Error(userFriendlyMessage, e.code())
        } catch (e: java.net.UnknownHostException) {
            Resource.Error("İnternet bağlantınızı kontrol edin")
        }
        catch (e:Exception){
            Resource.Error("Beklenmeyen hata: ${e.localizedMessage}")
        }
    }

    suspend fun aiRequest(aiRequest: AiRequest):Resource<ApiResponse<AiResponse>>{
        return try {
            val response =ortakAkilDaoInterface.aiRequest(aiRequest)
            Resource.Success(response)
        }catch (e: retrofit2.HttpException){
            val errorBody=e.response()?.errorBody()?.string()
            val backendMessage= try {
                Gson().fromJson(errorBody,ErrorResponse::class.java).message
            }catch (ex:Exception){
                null
            }
            val userFriendlyMessage = backendMessage?:"Bir hata oluştu, lütfen tekrar deneyin"

            Resource.Error(userFriendlyMessage, e.code())
        } catch (e: java.net.UnknownHostException) {
            Resource.Error("İnternet bağlantınızı kontrol edin")
        }
        catch (e:Exception){
            Resource.Error("Beklenmeyen hata: ${e.localizedMessage}")
        }
    }

    suspend fun loadProfile():Resource<ApiResponse<User>>{
        return try {
            val response = ortakAkilDaoInterface.loadProfile()
            Resource.Success(response)
        }catch (e: retrofit2.HttpException){
            val errorBody=e.response()?.errorBody()?.string()
            val backendMessage= try {
                Gson().fromJson(errorBody,ErrorResponse::class.java).message
            }catch (ex:Exception){
                null
            }
            val userFriendlyMessage = backendMessage?:"Bir hata oluştu, lütfen tekrar deneyin"

            Resource.Error(userFriendlyMessage, e.code())
        } catch (e: java.net.UnknownHostException) {
            Resource.Error("İnternet bağlantınızı kontrol edin")
        }
        catch (e:Exception){
            Resource.Error("Beklenmeyen hata: ${e.localizedMessage}")
        }
    }
    suspend fun updateProfile(user: User):Resource<ApiResponse<User>>{
        return try {
            val response =ortakAkilDaoInterface.updateProfile(user)
            Resource.Success(response)
        }catch (e: retrofit2.HttpException){
            val errorBody=e.response()?.errorBody()?.string()
            val backendMessage= try {
                Gson().fromJson(errorBody,ErrorResponse::class.java).message
            }catch (ex:Exception){
                null
            }
            val userFriendlyMessage = backendMessage?:"Bir hata oluştu, lütfen tekrar deneyin"

            Resource.Error(userFriendlyMessage, e.code())
        } catch (e: java.net.UnknownHostException) {
            Resource.Error("İnternet bağlantınızı kontrol edin")
        }
        catch (e:Exception){
            Resource.Error("Beklenmeyen hata: ${e.localizedMessage}")
        }

    }
    suspend fun logout(refreshToken: String?):Resource<ApiResponse<Boolean>> {
        return try {
            val response = ortakAkilDaoInterface.logout(mapOf("refreshToken" to refreshToken!!))
            Resource.Success(response)
        } catch (e: retrofit2.HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val backendMessage = try {
                Gson().fromJson(errorBody, ErrorResponse::class.java).message
            } catch (ex: Exception) {
                null
            }
            val userFriendlyMessage = backendMessage ?: "Bir hata oluştu, lütfen tekrar deneyin"

            Resource.Error(userFriendlyMessage, e.code())
        } catch (e: java.net.UnknownHostException) {
            Resource.Error("İnternet bağlantınızı kontrol edin")
        } catch (e: Exception) {
            Resource.Error("Beklenmeyen hata: ${e.localizedMessage}")
        }
    }
}

