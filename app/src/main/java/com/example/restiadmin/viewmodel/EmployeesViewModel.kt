package com.example.restiadmin.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restiadmin.api.EmployeeApi
import com.example.restiadmin.data.UserInfo
import com.example.restiadmin.data.requestmodel.EmployeeRequest
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class EmployeesViewModel : ViewModel() {
    private var _employees = mutableStateListOf<UserInfo>()
    private var _requests = mutableStateListOf<EmployeeRequest>()
    private var _users = mutableStateListOf<UserInfo>()
    private var errorMessage: String by mutableStateOf("")

    val employees : List<UserInfo>
        get() = _employees

    val requests : List<EmployeeRequest>
        get() = _requests

    val users : List<UserInfo>
        get() = _users

    fun deleteEmployee(id: Long){

    }

    fun admin(context: Context): Boolean{
        val sharedPreferences = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
        return sharedPreferences.getString("ROLE", "").toString() == "ADMIN"
    }
    private fun getRestaurantId(context: Context) : Long {
        val sharedPreferences = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
        return sharedPreferences.getLong("REST_ID", 0)
    }
    fun addEmployeeRequest(id: Long, context: Context){
        viewModelScope.launch {
            try {
                val api = EmployeeApi.getInstance(context)
                val response = api.requestEmployee(getRestaurantId(context), id)?.awaitResponse()
                if(response?.isSuccessful == true){
                    Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show()
                    fetchData(context)
                }else{

                }
            } catch (e: Exception) {
                errorMessage = e.toString()
                Log.e("ERROR", errorMessage)
            }
        }
    }

    fun fetchData(context: Context){
        viewModelScope.launch {
            try {
                _requests.clear()
                _users.clear()
                _employees.clear()
                val api = EmployeeApi.getInstance(context)
                val response1 = api.requestRestaurant(getRestaurantId(context))?.awaitResponse()
                if(response1?.isSuccessful == true){
                    response1.body()?.forEach{r ->_requests.add(r)}
                }
                val response2 = api.findEmployees(getRestaurantId(context))?.awaitResponse()
                if(response2?.isSuccessful == true){
                    response2.body()?.forEach{e -> _employees.add(e)}
                }
                val response3 = api.findFreeUsers()?.awaitResponse()
                if(response3?.isSuccessful == true){
                    response3.body()?.forEach{e -> _users.add(e)}
                }
            } catch (e: Exception) {
                errorMessage = e.toString()
                Log.e("ERROR", errorMessage)
            }
        }

    }

}