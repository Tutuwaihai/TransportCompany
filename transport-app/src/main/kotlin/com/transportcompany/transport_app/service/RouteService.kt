package com.transportcompany.transport_app.service

import com.transportcompany.transport_app.dto.RouteDto
import com.transportcompany.transport_app.dto.mappers.RouteMapper
import com.transportcompany.transport_app.repository.RouteRepository
import com.transportcompany.transport_app.repository.CityRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class RouteService(
    private val routeRepository: RouteRepository,
    private val routeMapper: RouteMapper,
    private val cityRepository: CityRepository
) {

    fun filterRoutesByCityIds(fromCityId: Long?, toCityId: Long?): List<RouteDto> {
        fromCityId?.let {
            cityRepository.findById(it)
                .orElseThrow { EntityNotFoundException("Город отправления с id=$it не найден") }
        }
        
        toCityId?.let {
            cityRepository.findById(it)
                .orElseThrow { EntityNotFoundException("Город прибытия с id=$it не найден") }
        }

        val routes = routeRepository.findAllByIsDeleted(0).filter { route ->
            val fromMatches = fromCityId?.let { it == route.idCityFrom } ?: true
            val toMatches = toCityId?.let { it == route.idCityTo } ?: true
            fromMatches && toMatches
        }
        return routeMapper.toDtoList(routes)
    }

    fun getAllRoutes(): List<RouteDto> {
        val routes = routeRepository.findAllByIsDeleted(0)
        return routeMapper.toDtoList(routes)
    }

    fun getRoutesPage(pageable: Pageable): Page<RouteDto> {
        val page = routeRepository.findAllByIsDeleted(0, pageable)
        return page.map { routeMapper.toDtoList(listOf(it))[0] }
    }

    fun filterRoutesByCityIdsPaged(fromCityId: Long?, toCityId: Long?, pageable: Pageable): Page<RouteDto> {
        val page = routeRepository.findFiltered(fromCityId, toCityId, pageable)
        return page.map { routeMapper.toDtoList(listOf(it))[0] }
    }
}