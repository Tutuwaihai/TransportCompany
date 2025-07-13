package com.transportcompany.transport_app.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.transportcompany.transport_app.model.City

data class TransportDto(
    val id: Long,
    val model: String?,
    val licensePlate: String?,
    val tonnage: Double?,
    val cityTitle: String?,
    val modelType: Int?,
    val ownerType: Int?
)