package com.example.restiadmin.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restiadmin.api.RestaurantApi
import com.example.restiadmin.api.UserApi
import com.example.restiadmin.data.Restaurant
import com.example.restiadmin.data.User
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class ProfileViewModel : ViewModel() {
    private var _user = mutableStateOf(User(0L,"","","","",0L, "", "", "bearer"))
    private var _restaurant = mutableStateOf(Restaurant())
    private var _restaurantExist= mutableStateOf(false)
    var errorMessage: String by mutableStateOf("")

    val user : User
        get() = _user.value
    val restaurant: Restaurant
        get() = _restaurant.value

    val restaurantExist : Boolean
        get() = _restaurantExist.value

    private fun readId(context: Context): Long{
        val sharedPreferences = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
        return sharedPreferences.getLong("USER_ID", 0)!!
    }

    fun fetchDatas(context: Context){
        getUser(context)
        getRestaurant(context)
    }
    fun getUser(context: Context){
        viewModelScope.launch {
            val api = UserApi.getInstance(context)
            try {
                val id=readId(context)
                val call = api.finById(id)
                val response = call?.awaitResponse()

                if(response?.isSuccessful == true){
                    _user.value=response.body()!!
                }
                else{
                    Toast.makeText(context, "Hiba", Toast.LENGTH_SHORT ).show()
                }

            }catch(e: java.lang.Exception){
                errorMessage = e.message.toString()
                Log.d("ERROR ", errorMessage)
            }
        }
    }

    fun getRestaurant(context: Context){
        viewModelScope.launch {
            val api = RestaurantApi.getInstance(context)
            try {
                val id = readId(context)
                val call = api.finByUser(id)
                val response = call?.awaitResponse()


                if(response?.isSuccessful == true){
                    _restaurantExist.value=true
                    _restaurant.value=response.body()!!
                    val sharedPref : SharedPreferences = context.getSharedPreferences("USER",Context.MODE_PRIVATE)
                    val editor : SharedPreferences.Editor = sharedPref.edit()
                    editor.putLong("REST_ID", _restaurant.value.id)
                    editor.apply()
                }
                else{
                    _restaurantExist.value=false
                }

            }catch(e: java.lang.Exception){
                errorMessage = e.toString()
                Log.d("ERROR ", errorMessage)
            }
        }
    }

    fun save(restaurant: Restaurant, context: Context){
        viewModelScope.launch {
            try {
                val api = RestaurantApi.getInstance(context)
                val id = readId(context)
                val call = api.add(restaurant, id)
                val response = call?.awaitResponse()
                Log.d("RESPONSE", response.toString())
                if(response?.isSuccessful == true){
                    getRestaurant(context)
                }
                else{
                    Toast.makeText(context, response?.message(), Toast.LENGTH_SHORT ).show()
                }
            } catch (e: Exception) {
                errorMessage = e.toString()
                Log.e("ERROR", errorMessage)
            }
        }
    }

}