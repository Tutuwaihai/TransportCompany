package com.transportcompany.transport_app.dto

import java.time.LocalDateTime

data class SendingDto(
    val id: Long?,
    val issueDate: LocalDateTime?,
    val lastNotification: LocalDateTime?,
    val sendDate: LocalDateTime?,
    val docNum: String?,
    val cargoName: String?,
    val packaging: String?,
    val description: String?,
    val clientFrom: ClientDto?,
    val clientTo: ClientDto?,
    val cityFrom: CityDto?,
    val cityTo: CityDto?,
    val price: PriceDto?,
    val wareFrom: WarehouseDto?,
    val wareTo: WarehouseDto?
)
