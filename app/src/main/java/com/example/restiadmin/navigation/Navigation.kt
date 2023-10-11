package com.example.restiadmin.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.restiadmin.screen.LoginScreen

@Composable
fun Navigation(navController: NavHostController){
    NavHost(navController = navController,
            startDestination = Screen.LoginScreen.route){
        composable(route=Screen.LoginScreen.route){
            LoginScreen(navController = navController)
        }
    }
}