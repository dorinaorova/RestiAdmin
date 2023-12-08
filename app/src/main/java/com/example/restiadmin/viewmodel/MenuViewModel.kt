package com.example.restiadmin.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restiadmin.api.MenuApi
import com.example.restiadmin.data.MenuItem
import com.example.restiadmin.data.requestmodel.MenuItemRequest
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class MenuViewModel  : ViewModel() {
    private var _menu = mutableStateListOf<MenuItem>()
    private var _requests = mutableStateListOf<MenuItemRequest>()
    private var errorMessage: String by mutableStateOf("")
    val menu : List<MenuItem>
        get() = _menu

    val requests : List<MenuItemRequest>
        get() = _requests

    fun admin(context: Context): Boolean{
        val sharedPreferences = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
        return sharedPreferences.getString("ROLE", "").toString() == "ADMIN"
    }
    private fun getRestaurantId(context: Context) : Long {
        val sharedPreferences = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
        return sharedPreferences.getLong("REST_ID", 0)
    }

    fun getMenu(food: Boolean, context: Context){
        viewModelScope.launch {
            try {
                _menu.clear()
                val api = MenuApi.getInstance(context)
                val id = getRestaurantId(context)
                val response =
                    if (food) {
                        api.getMenuFood(id)?.awaitResponse()
                    } else {
                        api.getMenuDrink(id)?.awaitResponse()
                    }
                if (response?.isSuccessful == true) {
                    response.body()?.forEach { item -> _menu.add(item) }
                } else {
                    Toast.makeText(context, response?.message(), Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                errorMessage = e.toString()
                Log.e("ERROR", errorMessage)
            }
        }
    }

    fun getRequests(food: Boolean, context: Context){
        viewModelScope.launch {
            try {
                _requests.clear()
                val api = MenuApi.getInstance(context)
                val id = getRestaurantId(context)
                val response =
                    if (food) {
                        api.getMenuRequestFood(id)?.awaitResponse()
                    } else {
                        api.getMenuRequestDrink(id)?.awaitResponse()
                    }
                Log.d("RESPONSE", response.toString())
                if (response?.isSuccessful == true) {
                    response.body()?.forEach { item -> _requests.add(item) }
                } else {
                    Toast.makeText(context, response?.message(), Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                errorMessage = e.toString()
                Log.e("ERROR", errorMessage)
            }
        }
    }

    fun save(food: Boolean, item: MenuItem, context: Context){
        viewModelScope.launch {
            try {
                val api = MenuApi.getInstance(context)
                val id = getRestaurantId(context)
                val response =
                    if (food) {
                        api.addMenuRequestFood(id,item)?.awaitResponse()
                    } else {
                        api.addMenuRequestDrink(id, item)?.awaitResponse()
                    }
                if (response?.isSuccessful == true) {
                    Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show()
                    getMenu(food, context)
                    getRequests(food, context)
                } else {
                    Toast.makeText(context, response?.message(), Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                errorMessage = e.toString()
                Log.e("ERROR", errorMessage)
            }
        }
    }

    fun addMenuItem(id: Long, context: Context, food: Boolean){
        viewModelScope.launch {
            try {
                val api = MenuApi.getInstance(context)
                val restId = getRestaurantId(context)
                val response =
                    if (food) {
                        api.addMenuFood(id, restId)?.awaitResponse()
                    } else {
                        api.addMenuDrink(id, restId)?.awaitResponse()
                    }
                Log.d("RESPONSE", response.toString())
                if(response?.isSuccessful==true){
                    Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show()
                    getMenu(food, context)
                    getRequests(food, context)
                }else {
                    Toast.makeText(context, response?.message(), Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                errorMessage = e.toString()
                Log.e("ERROR", errorMessage)
            }
        }
    }
    fun deleteRequest(id: Long, context: Context, food: Boolean){
        viewModelScope.launch {
            try {
                val api = MenuApi.getInstance(context)
                val restId = getRestaurantId(context)
                val response =
                    if (food) {
                        api.deleteRequestFood(id, restId)?.awaitResponse()
                    } else {
                        api.deleteRequestDrink(id, restId)?.awaitResponse()
                    }
                if(response?.isSuccessful==true){
                    Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show()
                    getMenu(food, context)
                    getRequests(food, context)
                }else {
                    Toast.makeText(context, response?.message(), Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                errorMessage = e.toString()
                Log.e("ERROR", errorMessage)
            }
        }
    }

}