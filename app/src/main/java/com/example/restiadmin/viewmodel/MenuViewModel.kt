package com.example.restiadmin.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.restiadmin.data.MenuItem

class MenuViewModel  : ViewModel() {
    private var _menu = mutableStateListOf<MenuItem>()
    var errorMessage: String by mutableStateOf("")
    val menu : List<MenuItem>
        get() = _menu

    fun getMenu(food: Boolean){

    }

    fun save(title: String, item: MenuItem){

    }
}