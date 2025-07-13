package com.transportcompany.transport_app.controller

import com.transportcompany.transport_app.service.EmployeeService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/drivers")
class DriverController(
    private val driverService: EmployeeService
) {

    @GetMapping
    fun getAllDrivers() = driverService.getAllDrivers()
}