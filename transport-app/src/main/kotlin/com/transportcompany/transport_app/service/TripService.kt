package com.transportcompany.transport_app.service

import com.transportcompany.transport_app.dto.CreateTripRequest
import com.transportcompany.transport_app.dto.TripResponse
import com.transportcompany.transport_app.dto.mappers.TripMapper
import com.transportcompany.transport_app.model.Trip
import com.transportcompany.transport_app.repository.*
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.Date
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

@Service
class TripService(
    private val tripRepository: TripRepository,
    private val cityRepository: CityRepository,
    private val warehouseRepository: WarehouseRepository,
    private val tripTypeRepository: TripTypeRepository,
    private val tripMapper: TripMapper,
    private val employeeRepository: EmployeeRepository
) {

    fun createTrip(request: CreateTripRequest): TripResponse {
        val username = SecurityContextHolder.getContext().authentication?.name
            ?: throw IllegalStateException("Пользователь не авторизован")
        val user = employeeRepository.findByEmail(username)
            ?: throw IllegalArgumentException("Пользователь с email $username не найден")
        val trip = tripMapper.toEntity(request, user.id)
        return tripMapper.toResponse(tripRepository.save(trip))
    }

    fun updateTrip(id: Long, request: CreateTripRequest): TripResponse {
        val username = SecurityContextHolder.getContext().authentication?.name
            ?: throw IllegalStateException("Пользователь не авторизован")
        val user = employeeRepository.findByEmail(username)
            ?: throw IllegalArgumentException("Пользователь с email $username не найден")
        val trip = tripRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Рейс с id=$id не найден") }
        val updatedTrip = tripMapper.updateTripFromRequest(request, trip, user.id)
        return tripMapper.toResponse(tripRepository.save(updatedTrip))
    }

    fun getAll(pageable: Pageable, startDate: LocalDateTime?, endDate: LocalDateTime?): Page<TripResponse> {
        val trips = if (startDate != null && endDate != null) {
            tripRepository.findAllTripsWithDateRange(startDate, endDate, pageable)
        } else {
            tripRepository.findAllTrips(pageable)
        }
        return trips.map(tripMapper::toResponse)
    }

    fun getById(id: Long): TripResponse {
        val trip = tripRepository.findById(id)
            .orElseThrow { NoSuchElementException("Рейс с id=$id не найден") }
        return tripMapper.toResponse(trip)
    }
}