package com.transportcompany.transport_app.controller

import com.transportcompany.transport_app.dto.WarehouseDto
import com.transportcompany.transport_app.service.WarehouseService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/warehouses")
class WarehouseController(
    private val wareHouseService: WarehouseService
) {
    @GetMapping
    fun getByCity(@RequestParam cityId: Long): List<WarehouseDto> = wareHouseService.getByCity(cityId)
}