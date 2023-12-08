package com.example.restiadmin.data

data class User(
    var id: Long,
    var name: String,
    var email: String,
    var phone: String?,
    var password: String?,
    var birthDate: Long?,
    var role: String,
    var accessToken: String,
    var tokenType: String
)