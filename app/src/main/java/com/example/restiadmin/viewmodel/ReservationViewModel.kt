package com.example.restiadmin.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.restiadmin.data.Reservation

class ReservationViewModel: ViewModel() {
    private var _current = mutableStateListOf<Reservation>()
    private var _past = mutableStateListOf<Reservation>()

    val current: List<Reservation>
        get() = _current
    val past: List<Reservation>
        get()=_past
}