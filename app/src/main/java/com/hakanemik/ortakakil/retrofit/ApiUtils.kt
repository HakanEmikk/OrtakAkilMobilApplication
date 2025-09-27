package com.hakanemik.ortakakil.retrofit

class ApiUtils {
    companion object{
        val BASE_URL="http://10.0.2.2:5000"
        fun getOrtakAkilDaoInterface() : OrtakAkilDaoInterface{
            return RetrofitClient.getClient(BASE_URL).create(OrtakAkilDaoInterface::class.java)
        }
    }
}