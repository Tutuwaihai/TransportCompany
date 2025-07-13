package com.transportcompany.transport_app.dto.mappers

import com.transportcompany.transport_app.dto.TripStateDto
import com.transportcompany.transport_app.model.TripState
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface TripStateMapper {
    fun toDto(entity: TripState): TripStateDto
    fun toDtoList(entities: List<TripState>): List<TripStateDto>
} 