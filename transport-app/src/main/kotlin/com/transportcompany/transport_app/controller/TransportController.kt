package com.transportcompany.transport_app.controller

import com.transportcompany.transport_app.service.TransportService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/transport")
class TransportController(
    private val transportService: TransportService
) {

    @GetMapping
    fun getAllTransport() = transportService.getAllTransport()

    @GetMapping("/page")
    fun getTransportPage(pageable: Pageable): Page<com.transportcompany.transport_app.dto.TransportDto> = transportService.getTransportPage(pageable)

    @GetMapping("/drivers/page")
    fun getDriversPage(
        pageable: Pageable,
        @RequestParam(required = false) cityId: Long?,
        @RequestParam(required = false) onlyDrivers: Boolean = false
    ): Page<com.transportcompany.transport_app.dto.EmployeeDto> = transportService.getDriversPage(pageable, cityId, onlyDrivers)

    @GetMapping("/trailers")
    fun getAllTrailers() = transportService.getAllTrailers()

    @GetMapping("/trailers/page")
    fun getTrailersPage(
        pageable: Pageable,
        @RequestParam(required = false) cityId: Long?,
        @RequestParam(required = false) number: String?
    ): Page<com.transportcompany.transport_app.dto.TransportDto> = transportService.getTrailersPage(pageable, cityId, number)
}