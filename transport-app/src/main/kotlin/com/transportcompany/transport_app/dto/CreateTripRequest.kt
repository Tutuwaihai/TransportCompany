package com.transportcompany.transport_app.dto

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.Column
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import java.math.BigDecimal
import java.util.*

data class CreateTripRequest(
    val description: String? = null,
    @field:NotNull(message = "ID города отправления обязателен")
    val cityFromId: Long? = null,
    @field:NotNull(message = "ID города прибытия обязателен")
    val cityToId: Long? = null,
    @field:NotNull(message = "ID склада отправления обязателен")
    val warehouseFromId: Long? = null,
    @field:NotNull(message = "ID склада прибытия обязателен")
    val warehouseToId: Long? = null,
    @field:NotNull(message = "Тип рейса обязателен")
    val tripTypeId: Long? = null,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    val dispatchDate: Date? = null,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    val arrivalDate: Date? = null,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    val expectedDate: Date? = null,
    val docNum: String?,
    val isTransit: Int,
    @field:Pattern(regexp = "^[0-4]$", message = "Состояние рейса должно быть числом от 0 до 4")
    val state: String? = null,
    val costs: BigDecimal?,
    val isCityCosts: Int,
    var employeeId: Long?,
    var transportId: Long?,
    val cargoSeal: String?
)