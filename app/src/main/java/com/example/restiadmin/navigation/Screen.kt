package com.example.restiadmin.navigation

sealed class Screen (val route: String){
    object LoginScreen: Screen("login_screen")
}