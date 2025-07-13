package com.transportcompany.transport_app.service

import com.transportcompany.transport_app.dto.FirmDto
import com.transportcompany.transport_app.dto.mappers.FirmMapper
import com.transportcompany.transport_app.repository.FirmRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class FirmService(
    private val firmRepository: FirmRepository,
    private val firmMapper: FirmMapper
) {
    fun getAllFirms(): List<FirmDto> {
        val firms = firmRepository.findAllByIsDeleted(0)
        return firmMapper.toDtoList(firms)
    }

    fun getFirmsPage(pageable: Pageable): Page<FirmDto> {
        val page = firmRepository.findAllByIsDeleted(0, pageable)
        return page.map { firmMapper.toDtoList(listOf(it))[0] }
    }
}