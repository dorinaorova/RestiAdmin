package com.example.restiadmin.viewmodel

import androidx.lifecycle.ViewModel
import com.example.restiadmin.data.User

class EmployeesViewModel : ViewModel() {
    private var _employees = mutableListOf<User>()
    private var _users = mutableListOf<User>()


    val employees : List<User>
        get() = _employees

    val users : List<User>
        get() = _users

    fun deleteEmployee(employee: User){

    }

    fun save(employee: User){}
}