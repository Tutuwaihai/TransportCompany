package com.transportcompany.transport_app.dto

import com.transportcompany.transport_app.model.Employee
import com.transportcompany.transport_app.model.Route
import java.math.BigDecimal
import java.time.LocalDateTime

data class TripUnionResponse(
    val id: Long,
    val createDate: LocalDateTime?,
    val modifyDate: LocalDateTime?,
    val firmCustomer: FirmDto?,
    val firmCarrier: FirmDto?,
    val route: RouteDto?,
    val employee: EmployeeDto?,
    val transport: TransportDto?,
    val trailer: TransportDto?,
    val costs: BigDecimal?,
    val description: String?
)