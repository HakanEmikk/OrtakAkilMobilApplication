package com.hakanemik.ortakakil.repo

import android.util.Log
import com.google.gson.Gson
import com.hakanemik.ortakakil.entity.ErrorResponse
import com.hakanemik.ortakakil.entity.LoginApiResponse
import com.hakanemik.ortakakil.entity.LoginRequest
import com.hakanemik.ortakakil.entity.Resource
import com.hakanemik.ortakakil.retrofit.ApiUtils
import com.hakanemik.ortakakil.retrofit.OrtakAkilDaoInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrtakAkilDaoRepository {
    var ortakAkilDaoInterface: OrtakAkilDaoInterface
    init {
        ortakAkilDaoInterface=ApiUtils.getOrtakAkilDaoInterface()
    }
  suspend  fun login(loginRequest: LoginRequest): Resource<LoginApiResponse> {
   return try {
        val response=ortakAkilDaoInterface.login(loginRequest)
        Resource.Success(response)
    }catch (e: retrofit2.HttpException){
        val errorBody=e.response()?.errorBody()?.string()
        val backendMessage= try {
            Gson().fromJson(errorBody,ErrorResponse::class.java).message
        }catch (ex:Exception){
            null
        }
       val userFriendlyMessage = when (backendMessage) {
           "Password is required" -> "Lütfen şifrenizi girin"
           "Email is required" -> "Lütfen e-posta adresinizi girin"
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


}