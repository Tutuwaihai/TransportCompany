package com.transportcompany.transport_app.controller

import com.transportcompany.transport_app.dto.TripTypeDto
import com.transportcompany.transport_app.service.TripTypeService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/trip-types")
class TripTypeController(
    private val tripTypeService: TripTypeService
) {
    @GetMapping
    fun getAll(): List<TripTypeDto> = tripTypeService.getAll()
}