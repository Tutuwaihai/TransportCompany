package com.transportcompany.transport_app.service

import com.transportcompany.transport_app.dto.CityDto
import com.transportcompany.transport_app.dto.mappers.CityMapper
import com.transportcompany.transport_app.repository.CityRepository
import org.springframework.stereotype.Service

@Service
class CityService(
    private val cityRepository: CityRepository,
    private val cityMapper: CityMapper
) {
    fun getAllActiveCities(): List<CityDto> {
        val cities = cityRepository.findAllByIsDeleted(0)
        return cityMapper.toDtoList(cities)
    }
}