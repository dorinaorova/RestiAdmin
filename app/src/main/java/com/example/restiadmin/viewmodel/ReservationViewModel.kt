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
import com.example.restiadmin.api.ReservationApi
import com.example.restiadmin.data.StateEnum
import com.example.restiadmin.data.requestmodel.ReservationRequest
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import java.util.Date

class ReservationViewModel: ViewModel() {
    private var _current = mutableStateListOf<ReservationRequest>()
    private var _past = mutableStateListOf<ReservationRequest>()
    private var errorMessage: String by mutableStateOf("")

    val current: List<ReservationRequest>
        get() = _current
    val past: List<ReservationRequest>
        get()=_past

    private fun getRestaurantId(context: Context) : Long {
        val sharedPreferences = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
        return sharedPreferences.getLong("REST_ID", 0)
    }
    fun fetchData(context: Context){
        viewModelScope.launch {
            try {
                _current.clear()
                _past.clear()
                val id = getRestaurantId(context)
                val api = ReservationApi.getInstance(context)
                val call = api.findRequests(id)
                val response = call?.awaitResponse()
                if(response?.isSuccessful ==true){
                    val currentDate = Date().time
                    val requests = response.body()
                    requests?.filter{r -> r.reservation!!.date>=currentDate}?.forEach{ r -> _current.add(r)}
                    requests?.filter{r -> r.reservation!!.date<currentDate}?.forEach{ r -> _past.add(r)}
                }
                else{
                    Toast.makeText(context, response?.message(), Toast.LENGTH_SHORT ).show()
                }
            } catch (e: Exception) {
                errorMessage = e.toString()
                Log.e("ERROR", errorMessage)
            }
        }
    }

    fun update(id: Long, context: Context, note: String?, state: StateEnum){
        viewModelScope.launch {
            try {
                val api = ReservationApi.getInstance(context)
                val reservationRequest = ReservationRequest(id, null, state, note)
                val response = api.updateRequest(id, reservationRequest)?.awaitResponse()
                if(response?.isSuccessful==true){
                    fetchData(context)
                }
                else{
                    Toast.makeText(context, response?.message(), Toast.LENGTH_SHORT ).show()
                }
            } catch (e: Exception) {
                errorMessage = e.toString()
                Log.e("ERROR", errorMessage)
            }
        }
    }

}