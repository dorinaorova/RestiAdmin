package com.example.restiadmin.data.requestmodel

import com.example.restiadmin.data.Restaurant
import com.example.restiadmin.data.User

data class EmployeeRequest(
    val id: Long,
    val restaurant: Restaurant,
    val user: User
)
