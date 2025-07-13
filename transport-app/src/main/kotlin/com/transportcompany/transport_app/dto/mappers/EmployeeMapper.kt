package com.transportcompany.transport_app.dto.mappers

import com.transportcompany.transport_app.dto.EmployeeDto
import com.transportcompany.transport_app.model.Employee
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
interface EmployeeMapper {
    fun toDtoList(employees: List<Employee>): List<EmployeeDto>
    fun toDto(employee: Employee): EmployeeDto
}