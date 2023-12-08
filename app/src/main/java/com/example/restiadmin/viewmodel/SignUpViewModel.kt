package com.example.restiadmin.viewmodel

import android.content.Context
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

class SignUpViewModel : ViewModel() {
    var errorMessage: String by mutableStateOf("")

    fun signUp(user: User, navController: NavController, context: Context){
        viewModelScope.launch{
            try{
                val api = LoginApi.getInstance()
                val call = api.signup(user)
                val response = call?.awaitResponse()

                if(response?.isSuccessful == true){
                    Toast.makeText(context,"Sikeres regisztráció", Toast.LENGTH_SHORT).show()
                    navController.navigate(route = Screen.LoginScreen.route)
                    val sharedPref : SharedPreferences = context.getSharedPreferences("PREFERENCE_NAME",
                        Context.MODE_PRIVATE
                    )
                    val editor : SharedPreferences.Editor = sharedPref.edit()
                    editor.putLong("USER_ID", response.body()!!.id);
                    editor.commit()
                }
                else{
                    Toast.makeText(context, "Ezzel az email-címmel már létezik regisztrált felhasználó", Toast.LENGTH_SHORT ).show()
                    Log.d("HIBA ", response?.code().toString())
                }
            }catch(e: java.lang.Exception){
                errorMessage = e.message.toString()
                Log.d("ERROR ", errorMessage)
            }
        }
    }
}