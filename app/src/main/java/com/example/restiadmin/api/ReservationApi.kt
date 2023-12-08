package com.example.restiadmin.api

import android.content.Context
import com.example.restiadmin.data.requestmodel.ReservationRequest
import com.example.restiadmin.interceptor.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

private const val BASE_URL = "http://152.66.183.63:8080/reservation/"
interface ReservationApi {

    @GET("findbyrestaurant/{id}")
    abstract fun findRequests(@Path("id") id: Long) : Call<List<ReservationRequest>>?

    @POST("update/{id}")
    abstract fun updateRequest(@Path("id") id: Long, @Body request: ReservationRequest) : Call<ReservationRequest>?

    companion object {
        private var apiService: ReservationApi? = null
        fun getInstance(context: Context): ReservationApi {
            val authInterceptor = AuthInterceptor(context)

            if (ReservationApi.apiService == null) {
                ReservationApi.apiService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(
                        OkHttpClient.Builder()
                            .addInterceptor(authInterceptor)
                            .build()
                    )
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(ReservationApi::class.java)
            }
            return ReservationApi.apiService!!
        }
    }
}