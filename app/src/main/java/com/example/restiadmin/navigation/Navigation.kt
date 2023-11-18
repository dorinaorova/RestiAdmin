package com.example.restiadmin.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.restiadmin.screen.EmployeesScreen
import com.example.restiadmin.screen.LoginScreen
import com.example.restiadmin.screen.MenuScreen
import com.example.restiadmin.screen.ProfileScreen
import com.example.restiadmin.screen.ReservationScreen
import com.example.restiadmin.screen.SignUpScreen


@Composable
fun Navigation(navController: NavHostController){
    NavHost(navController = navController,
            startDestination = Screen.LoginScreen.route){
        composable(route=Screen.LoginScreen.route){
            LoginScreen(navController = navController)
        }
        composable(route=Screen.ProfileScreen.route){
            ProfileScreen(navController=navController)
        }
        composable(route=Screen.SignUpScreen.route){
            SignUpScreen(navController = navController)
        }
        composable(route=Screen.MenuScreen.route+"/{title}"){
            MenuScreen(navController= navController, title = it.arguments?.getString("title").toString())
        }
        composable(route=Screen.ReservationScreen.route){
            ReservationScreen(navController = navController)
        }
        composable(route=Screen.EmployeesScreen.route){
            EmployeesScreen(navController = navController)
        }
    }
}