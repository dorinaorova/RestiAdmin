package com.example.restiadmin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.restiadmin.ui.theme.RestiAdminTheme
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.restiadmin.navigation.Navigation

lateinit var navController : NavHostController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RestiAdminTheme {
                navController = rememberNavController()
               Navigation(navController = navController)
            }
        }
    }
}

