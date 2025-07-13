package com.transportcompany.transport_app.dto.mappers

import com.transportcompany.transport_app.dto.TransportDto
import com.transportcompany.transport_app.model.Transport
import org.springframework.stereotype.Component

@Component
class TransportMapper {
    fun toDto(entity: Transport): TransportDto = TransportDto(
        id = entity.id,
        model = entity.modelTransport?.model,
        licensePlate = entity.licensePlate,
        tonnage = entity.modelTransport?.tonnage,
        cityTitle = entity.city?.title,
        modelType = entity.modelTransport?.type,
        ownerType = entity.type
    )

    fun toDtoList(entities: List<Transport>): List<TransportDto> = entities.map { toDto(it) }
}