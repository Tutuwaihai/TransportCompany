package com.transportcompany.transport_app.dto.mappers

import com.transportcompany.transport_app.dto.ModelTransportDto
import com.transportcompany.transport_app.model.ModelTransport
import com.transportcompany.transport_app.model.Transport
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface ModelTransportMapper {

    fun toDtoList(entities: List<ModelTransport>): List<ModelTransportDto>

    fun toDto(entity: Transport): ModelTransportDto = ModelTransportDto(
        id = entity.id,
        model = entity.modelTransport?.model,
        tonnage = entity.modelTransport?.tonnage,
        volume = entity.modelTransport?.volume,
        cityTitle = entity.city?.title
    )

    fun toDtoListFromTransport(entities: List<Transport>): List<ModelTransportDto> = entities.map { toDto(it) }
}