package com.transportcompany.transport_app.service

import com.transportcompany.transport_app.dto.TripTypeDto
import com.transportcompany.transport_app.dto.mappers.TripTypeMapper
import com.transportcompany.transport_app.repository.TripTypeRepository
import org.springframework.stereotype.Service

@Service
class TripTypeService(
    private val tripTypeRepository: TripTypeRepository,
    private val tripTypeMapper: TripTypeMapper
) {
    fun getAll(): List<TripTypeDto> = tripTypeMapper.toTripTypeDtoList(tripTypeRepository.findAllActive())
}