package com.example.restiadmin.data.requestmodel

import com.example.restiadmin.data.Reservation
import com.example.restiadmin.data.StateEnum

data class ReservationRequest(
    val id: Long,
    val reservation: Reservation?,
    val state: StateEnum,
    val note: String?
)
