package com.example.restiadmin.navigation

sealed class Screen (val route: String){
    object LoginScreen: Screen("login_screen")
    object ProfileScreen: Screen("profile_screen")
    object SignUpScreen: Screen("signup_screen")
    object ReservationScreen: Screen("reservation_screen")
    object MenuScreen: Screen("menu_screen")
    object EmployeesScreen: Screen("employees_screen")
}