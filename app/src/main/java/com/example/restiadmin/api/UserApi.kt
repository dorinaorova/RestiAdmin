package com.example.restiadmin.api

import android.content.Context
import com.example.restiadmin.data.User
import com.example.restiadmin.interceptor.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

private const val BASE_URL = "http://152.66.183.63:8080/user/"
interface UserApi {

    @Headers("Accept: application/json")
    @GET("findbyid/{id}")
    fun finById(@Path("id") id: Long) : Call<User>?


    companion object {
        var apiService: UserApi? = null
        fun getInstance(context : Context): UserApi {
            val authInterceptor = AuthInterceptor(context)

            if (apiService == null) {
                apiService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(
                        OkHttpClient.Builder()
                            .addInterceptor(authInterceptor)
                            .build()
                    )
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(UserApi::class.java)
            }
            return apiService!!
        }
    }
}