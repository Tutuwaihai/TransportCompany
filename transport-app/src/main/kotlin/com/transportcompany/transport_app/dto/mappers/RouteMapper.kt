package com.transportcompany.transport_app.dto.mappers

import com.transportcompany.transport_app.dto.RouteDto
import com.transportcompany.transport_app.model.Route
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface RouteMapper {
    fun toDtoList(routes: List<Route>): List<RouteDto>
}