package com.transportcompany.transport_app.dto.mappers

import com.transportcompany.transport_app.dto.PriceDto
import com.transportcompany.transport_app.model.Price
import org.mapstruct.Mapper
 
@Mapper(componentModel = "spring")
interface PriceMapper {
    fun toDto(entity: Price?): PriceDto?
} 