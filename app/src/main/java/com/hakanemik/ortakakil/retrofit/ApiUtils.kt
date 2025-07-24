package com.hakanemik.ortakakil.retrofit

class ApiUtils {
    companion object{
        val Base_URL=""
        fun getOrtakAkilDaoInterface():OrtakAkilDaoInterface{
            return RetrofitClient.getClient(Base_URL).create(OrtakAkilDaoInterface::class.java)
        }
    }
}