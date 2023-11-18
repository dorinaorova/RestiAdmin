package com.example.restiadmin.api

import com.example.restiadmin.data.User
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

private const val BASE_URL = "http://152.66.183.24:8080/auth/"

interface LoginApi {
    @Headers("Accept: application/json")
    @POST("login")
    abstract fun login(@Body user: User) : Call<User?>?
    @POST("signup")

    abstract fun signup(@Body user: User): Call<User>?

    companion object {
        var apiService: LoginApi? = null
        fun getInstance(): LoginApi {
            if (apiService == null) {
                apiService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(LoginApi::class.java)
            }
            return apiService!!
        }

    }
}