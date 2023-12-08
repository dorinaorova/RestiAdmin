package com.example.restiadmin.api

import android.content.Context
import com.example.restiadmin.data.MenuItem
import com.example.restiadmin.data.requestmodel.MenuItemRequest
import com.example.restiadmin.interceptor.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

private const val BASE_URL = "http://152.66.183.63:8080/menu/"
interface MenuApi {

    @GET("findmenu/food/{id}")
    abstract fun getMenuFood(@Path("id") id: Long) : Call<List<MenuItem>>?
    @GET("findmenu/drink/{id}")
    abstract fun getMenuDrink(@Path("id") id: Long) : Call<List<MenuItem>>?
    @GET("findmenu/request/food/{restaurantId}")
    abstract fun getMenuRequestFood(@Path("restaurantId") id: Long) : Call<List<MenuItemRequest>>?
    @GET("findmenu/request/drink/{restaurantId}")
    abstract fun getMenuRequestDrink(@Path("restaurantId") id: Long) : Call<List<MenuItemRequest>>?
    @POST("addmenu/food/{restaurantId}/{id}")
    abstract fun addMenuFood(@Path("id") id: Long, @Path("restaurantId") restaurantId: Long) : Call<MenuItem>?
    @POST("addmenu/drink/{restaurantId}/{id}")
    abstract fun addMenuDrink(@Path("id") id: Long, @Path("restaurantId") restaurantId: Long) : Call<MenuItem>?
    @POST("addmenu/request/food/{id}")
    abstract fun addMenuRequestFood(@Path("id") id: Long, @Body food: MenuItem) : Call<MenuItemRequest>?
    @POST("addmenu/request/drink/{id}")
    abstract fun addMenuRequestDrink(@Path("id") id: Long, @Body drink: MenuItem) : Call<MenuItemRequest>?
    @DELETE("/menu/delete/request/food/{restaurantId}/{id}")
    abstract fun deleteRequestFood(@Path("id") id: Long, @Path("restaurantId") restaurantId: Long) : Call<Any>?
    @DELETE("/menu/delete/request/drink/{restaurantId}/{id}")
    abstract fun deleteRequestDrink(@Path("id") id: Long, @Path("restaurantId") restaurantId: Long) : Call<Any>?

    companion object {
        var apiService: MenuApi? = null
        fun getInstance(context: Context): MenuApi {
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
                    .build().create(MenuApi::class.java)
            }
            return apiService!!
        }
    }
}