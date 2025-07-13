package com.transportcompany.transport_app.service

import com.transportcompany.transport_app.dto.EmployeeDto
import com.transportcompany.transport_app.dto.mappers.EmployeeMapper
import com.transportcompany.transport_app.repository.EmployeeRepository
import org.springframework.stereotype.Service

@Service
class EmployeeService(
    private val employeeRepository: EmployeeRepository,
    private val employeeMapper: EmployeeMapper
) {
    fun getAllDrivers(): List<EmployeeDto> {
        val employees = employeeRepository.findAllDrivers()
        return employeeMapper.toDtoList(employees)
    }
}