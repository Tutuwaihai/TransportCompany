package com.transportcompany.transport_app.dto.mappers

import com.transportcompany.transport_app.dto.FirmDto
import com.transportcompany.transport_app.model.Firm
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface FirmMapper {
    fun toDtoList(firms: List<Firm?>): List<FirmDto>
}