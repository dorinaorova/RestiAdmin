package com.example.restiadmin.api

import android.content.Context
import com.example.restiadmin.data.Restaurant
import com.example.restiadmin.interceptor.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path


private const val BASE_URL = "http://152.66.183.63:8080/restaurant/"
interface RestaurantApi {
    @Headers("Accept: application/json")
    @POST("add/{adminId}")
    abstract fun add(@Body restaurant: Restaurant, @Path("adminId") id: Long) : Call<Restaurant>?

    @Headers("Accept: application/json")
    @GET("findbyuser/{id}")
    abstract fun finByUser(@Path("id") id: Long): Call<Restaurant>?
    companion object {
        var apiService: RestaurantApi? = null

        fun getInstance(context : Context): RestaurantApi {
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
                    .build().create(RestaurantApi::class.java)
            }
            return apiService!!
        }
    }
}