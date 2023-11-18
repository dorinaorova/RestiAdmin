package com.example.restiadmin.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.restiadmin.data.Restaurant
import com.example.restiadmin.data.User

class ProfileViewModel : ViewModel() {
    private var _user = mutableStateOf(User(0L,"","","","",0L, "", "", "bearer"))
    private var _restaurant = mutableStateOf(Restaurant())
    private var _restaurantExist= mutableStateOf(false)

    val user : User
        get() = _user.value
    val restaurant: Restaurant
        get() = _restaurant.value

    val restaurantExist : Boolean
        get() = _restaurantExist.value

}