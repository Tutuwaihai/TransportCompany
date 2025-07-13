package com.transportcompany.transport_app.dto.mappers

import com.transportcompany.transport_app.dto.ClientDto
import com.transportcompany.transport_app.model.Client
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface ClientMapper {
    fun toDto(entity: Client?): ClientDto?
} 