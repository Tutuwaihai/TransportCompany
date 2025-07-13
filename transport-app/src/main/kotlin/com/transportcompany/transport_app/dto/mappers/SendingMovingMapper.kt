package com.transportcompany.transport_app.dto.mappers

import com.transportcompany.transport_app.dto.SendingMovingDto
import com.transportcompany.transport_app.model.SendingMoving
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring", uses = [SendingMapper::class])
interface SendingMovingMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "idsending", source = "sending")
    fun toDto(entity: SendingMoving): SendingMovingDto

    fun toDtoList(entities: List<SendingMoving>): List<SendingMovingDto>
}