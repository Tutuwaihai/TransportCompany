package com.transportcompany.transport_app.dto.mappers

import com.transportcompany.transport_app.dto.TripTypeDto
import com.transportcompany.transport_app.model.TripType
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface TripTypeMapper {
    fun toTripTypeDtoList(list: List<TripType>): List<TripTypeDto>
}