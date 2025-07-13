package com.transportcompany.transport_app.dto.mappers

import com.transportcompany.transport_app.dto.WarehouseDto
import com.transportcompany.transport_app.model.Warehouse
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface WarehouseMapper {
    fun toWareHouseDtoList(list: List<Warehouse>): List<WarehouseDto>
    fun toDto(entity: Warehouse?): WarehouseDto?
}