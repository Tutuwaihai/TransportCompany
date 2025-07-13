package com.transportcompany.transport_app.dto.mappers

import com.transportcompany.transport_app.dto.SendingDto
import com.transportcompany.transport_app.model.Sending
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring", uses = [
    ClientMapper::class,
    CityMapper::class,
    PriceMapper::class,
    WarehouseMapper::class
])
interface SendingMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "issueDate", source = "issueDate")
    @Mapping(target = "lastNotification", source = "lastNotification")
    @Mapping(target = "sendDate", source = "sendDate")
    @Mapping(target = "docNum", source = "docNum")
    @Mapping(target = "cargoName", source = "cargoName")
    @Mapping(target = "packaging", source = "packaging")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "clientFrom", source = "clientFrom")
    @Mapping(target = "clientTo", source = "clientTo")
    @Mapping(target = "cityFrom", source = "cityFrom")
    @Mapping(target = "cityTo", source = "cityTo")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "wareFrom", source = "wareFrom")
    @Mapping(target = "wareTo", source = "wareTo")
    fun toDto(entity: Sending?): SendingDto?
} 