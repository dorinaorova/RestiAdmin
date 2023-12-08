package com.example.restiadmin.data

data class UserInfo(
    val id : Long,
    val name: String,
    val email: String,
    val phone : String?,
    val birthDate: Long?
)
