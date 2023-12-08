package com.example.restiadmin.api

import android.content.Context
import com.example.restiadmin.data.Restaurant
import com.example.restiadmin.data.UserInfo
import com.example.restiadmin.data.requestmodel.EmployeeRequest
import com.example.restiadmin.interceptor.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

private const val BASE_URL = "http://152.66.183.63:8080/employee/"
interface EmployeeApi {

    @POST("addemployee")
    fun addEmployee(@Body request: EmployeeRequest) : Call<Restaurant>?

    @POST("request/{restaurantId}/{userId}")
    fun requestEmployee(@Path("restaurantId") restaurantId: Long,@Path("userId") userId: Long ) : Call<EmployeeRequest>?

    @GET("request/user/{id}")
    fun requestOwn(@Path("id") id: Long) : Call<List<EmployeeRequest>>?

    @GET("request/restaurant/{id}")
    fun requestRestaurant(@Path("id") id: Long) : Call<List<EmployeeRequest>>?

    @GET("employee/{id}")
    fun findEmployees(@Path("id") id: Long) : Call<List<UserInfo>>?

    @GET("employees/free")
    fun findFreeUsers(): Call<List<UserInfo>>?

    companion object {
        var apiService: EmployeeApi? = null
        fun getInstance(context : Context): EmployeeApi {
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
                    .build().create(EmployeeApi::class.java)
            }
            return apiService!!
        }
    }
}