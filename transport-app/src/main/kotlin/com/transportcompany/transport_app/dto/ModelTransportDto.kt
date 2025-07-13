package com.transportcompany.transport_app.dto

data class ModelTransportDto(
    val id: Long,
    val model: String?,
    val tonnage: Double?,
    val volume: Double?,
    val cityTitle: String?
)
