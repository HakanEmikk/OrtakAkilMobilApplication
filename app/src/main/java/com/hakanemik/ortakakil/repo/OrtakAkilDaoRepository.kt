package com.hakanemik.ortakakil.repo


import com.google.gson.Gson
import com.hakanemik.ortakakil.entity.AiApiResponse
import com.hakanemik.ortakakil.entity.AiRequest
import com.hakanemik.ortakakil.entity.ErrorResponse
import com.hakanemik.ortakakil.entity.LoginApiResponse
import com.hakanemik.ortakakil.entity.LoginRequest
import com.hakanemik.ortakakil.entity.RegisterApiResponse
import com.hakanemik.ortakakil.entity.RegisterRequest
import com.hakanemik.ortakakil.entity.Resource
import com.hakanemik.ortakakil.retrofit.OrtakAkilDaoInterface
import javax.inject.Inject
import javax.inject.Named

class OrtakAkilDaoRepository @Inject constructor(
    private val ortakAkilDaoInterface: OrtakAkilDaoInterface,
    @Named("authApi") private val authApi: OrtakAkilDaoInterface,
){

  suspend  fun login(loginRequest: LoginRequest): Resource<LoginApiResponse> {
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
    suspend fun register(registerRequest: RegisterRequest):Resource<RegisterApiResponse>{
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

    suspend fun aiRequest(aiRequest: AiRequest):Resource<AiApiResponse>{
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
            val userFriendlyMessage = when (backendMessage) {

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



}