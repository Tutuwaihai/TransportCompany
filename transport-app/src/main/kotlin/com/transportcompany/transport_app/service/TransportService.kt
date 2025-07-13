package com.transportcompany.transport_app.service

import com.transportcompany.transport_app.dto.TransportDto
import com.transportcompany.transport_app.dto.mappers.TransportMapper
import com.transportcompany.transport_app.repository.TransportRepository
import com.transportcompany.transport_app.repository.EmployeeRepository
import com.transportcompany.transport_app.dto.EmployeeDto
import com.transportcompany.transport_app.dto.mappers.EmployeeMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.PageImpl
import org.springframework.stereotype.Service

@Service
class TransportService(
    private val transportRepository: TransportRepository,
    private val transportMapper: TransportMapper,
    private val employeeRepository: EmployeeRepository,
    private val employeeMapper: EmployeeMapper
) {
    fun getAllTransport(): List<TransportDto> {
        val transports = transportRepository.findAllTransportWithModel()
        return transportMapper.toDtoList(transports)
    }

    fun getTransportPage(pageable: Pageable): Page<TransportDto> {
        val page = transportRepository.findAllTransportWithModelPaged(pageable)
        return page.map { transportMapper.toDto(it) }
    }

    fun getDriversPage(pageable: Pageable, cityId: Long?, onlyDrivers: Boolean): Page<EmployeeDto> {
        val workerTypes = if (onlyDrivers) listOf(2L, 4L) else null
        val page = employeeRepository.findAllByFilters(0, cityId, workerTypes, pageable)
        return page.map { employeeMapper.toDtoList(listOf(it))[0] }
    }

    fun getAllTrailers(): List<TransportDto> {
        val trailers = transportRepository.findAllTrailersWithModel()
        return transportMapper.toDtoList(trailers)
    }

    fun getTrailersPage(pageable: Pageable, cityId: Long?, number: String?): Page<TransportDto> {
        val page = transportRepository.findAllTrailersWithModelPaged(pageable, cityId, number)
        return page.map { transportMapper.toDto(it) }
    }
}