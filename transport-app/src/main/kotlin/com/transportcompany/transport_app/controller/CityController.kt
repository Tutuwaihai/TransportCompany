package com.transportcompany.transport_app.controller

import com.transportcompany.transport_app.dto.CityDto
import com.transportcompany.transport_app.service.CityService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/cities")
class CityController(
    private val cityService: CityService
) {
    @GetMapping
    fun getAllCities(): List<CityDto> = cityService.getAllActiveCities()
}