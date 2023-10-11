package com.example.restiadmin.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.restiadmin.data.User
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    private var _user = mutableStateOf(User(0L,"","","","",0L))
    var errorMessage: String by mutableStateOf("")
    val userId : Long
        get() = _user.value.id

    fun login(user: User, navController: NavController, context: Context){
        viewModelScope.launch {

        }
    }



}