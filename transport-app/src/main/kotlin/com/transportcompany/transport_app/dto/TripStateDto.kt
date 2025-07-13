package com.transportcompany.transport_app.dto

data class TripStateDto(
    val id: Long,
    val title: String,
    val description: String? = null
) 