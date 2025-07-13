package com.transportcompany.transport_app.controller

import com.transportcompany.transport_app.dto.RouteDto
import com.transportcompany.transport_app.service.RouteService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/routes")
class RouteController(
    private val routeService: RouteService
) {

    @GetMapping
    fun getAllRoutes(): List<RouteDto> = 
        routeService.getAllRoutes()

    @GetMapping("/page")
    fun getRoutesPage(pageable: Pageable): Page<RouteDto> = routeService.getRoutesPage(pageable)

    @GetMapping("/filter")
    fun filterRoutes(
        @RequestParam(required = false) fromCityId: Long?,
        @RequestParam(required = false) toCityId: Long?
    ) = routeService.filterRoutesByCityIds(fromCityId, toCityId)

    @GetMapping("/filter-page")
    fun filterRoutesPage(
        @RequestParam(required = false) fromCityId: Long?,
        @RequestParam(required = false) toCityId: Long?,
        pageable: Pageable
    ) = routeService.filterRoutesByCityIdsPaged(fromCityId, toCityId, pageable)
}
