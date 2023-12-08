package com.example.restiadmin.data

data class Reservation(
    var id: Long,
    var restaurant: Restaurant?,
    var user: User?,
    var date: Long,
    var time: Int,
    var people: Int
)