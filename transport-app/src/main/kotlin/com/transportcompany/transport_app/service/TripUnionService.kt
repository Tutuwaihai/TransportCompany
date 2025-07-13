package com.transportcompany.transport_app.service


import com.transportcompany.transport_app.dto.TripUnionRequest
import com.transportcompany.transport_app.dto.TripUnionResponse
import com.transportcompany.transport_app.dto.mappers.TripUnionMapper
import com.transportcompany.transport_app.model.TripUnion
import com.transportcompany.transport_app.repository.TripUnionRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.security.core.context.SecurityContextHolder
import com.transportcompany.transport_app.repository.EmployeeRepository
import java.time.LocalDateTime

@Service
class TripUnionService(
    private val tripUnionRepository: TripUnionRepository,
    private val tripUnionMapper: TripUnionMapper,
    private val employeeRepository: EmployeeRepository
) {

    fun createTrip(request: TripUnionRequest): TripUnionResponse {
        val username = SecurityContextHolder.getContext().authentication?.name
            ?: throw IllegalStateException("Пользователь не авторизован")
        val user = employeeRepository.findByEmail(username)
            ?: throw IllegalArgumentException("Пользователь с email $username не найден")
        val entity = tripUnionMapper.toEntity(request, user.id)
        val saved = tripUnionRepository.save(entity)
        return tripUnionMapper.toResponse(saved)
    }

    fun updateTrip(id: Long, request: TripUnionRequest): TripUnionResponse {
        val username = SecurityContextHolder.getContext().authentication?.name
            ?: throw IllegalStateException("Пользователь не авторизован")
        val user = employeeRepository.findByEmail(username)
            ?: throw IllegalArgumentException("Пользователь с email $username не найден")
        val entity = tripUnionRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Путевой лист с id=$id не найден") }
        val updated = tripUnionMapper.updateTripUnionFromRequest(request, entity, user.id)
        val saved = tripUnionRepository.save(updated)
        return tripUnionMapper.toResponse(saved)
    }

    fun getTripUnionById(id: Long): TripUnionResponse {
        val entity = tripUnionRepository.findById(id)
            .orElseThrow { NoSuchElementException("Путевой лист с id=$id не найден") }
        return tripUnionMapper.toResponse(entity)
    }

    fun getAllTripUnions(pageable: Pageable, startDate: LocalDateTime?, endDate: LocalDateTime?): Page<TripUnionResponse> {
        val page = when {
            startDate != null && endDate != null -> 
                tripUnionRepository.findAllActiveAndNotDeletedBetween(pageable, startDate, endDate)
            startDate != null -> 
                tripUnionRepository.findAllActiveAndNotDeletedFrom(pageable, startDate)
            endDate != null -> 
                tripUnionRepository.findAllActiveAndNotDeletedTo(pageable, endDate)
            else -> 
                tripUnionRepository.findAllActiveAndNotDeleted(pageable)
        }
        return page.map { tripUnionMapper.toResponse(it) }
    }
}
