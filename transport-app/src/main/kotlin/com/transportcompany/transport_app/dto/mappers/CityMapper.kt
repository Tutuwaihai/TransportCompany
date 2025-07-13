package com.transportcompany.transport_app.dto.mappers

import com.transportcompany.transport_app.dto.CityDto
import com.transportcompany.transport_app.model.City
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface CityMapper {
    fun toDtoList(cities: List<City>): List<CityDto>
    fun toDto(entity: City?): CityDto?
}