package com.example.restiadmin.viewmodel

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.restiadmin.api.LoginApi
import com.example.restiadmin.data.User
import com.example.restiadmin.navigation.Screen
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class LoginViewModel: ViewModel() {
    private var _user = mutableStateOf(User(0L,"","","","",0L,"","",""))
    var errorMessage: String by mutableStateOf("")
    val userId : Long
        get() = _user.value.id

    fun login(user: User, navController: NavController, context: Context){
        viewModelScope.launch {
            try{
                val api = LoginApi.getInstance()
                val call = api.login(user)
                val response = call?.awaitResponse()

                if(response?.isSuccessful == true){
                    _user.value = response.body()!!
                    navController.navigate(route = Screen.ProfileScreen.route)
                    val sharedPref : SharedPreferences = context.getSharedPreferences("USER",MODE_PRIVATE)
                    val editor : SharedPreferences.Editor = sharedPref.edit()
                    editor.putLong("USER_ID", userId);
                    editor.putString("TOKEN", _user.value.accessToken)
                    editor.putString("TYPE", _user.value.tokenType)
                    editor.putString("ROLE", _user.value.role)
                    editor.apply()
                }
                else{
                    Toast.makeText(context, "Hibás adatok", Toast.LENGTH_SHORT ).show()
                }
            }catch(e: java.lang.Exception){
                errorMessage = e.message.toString()
                Log.d("ERROR ", errorMessage)
            }
        }

    }



}