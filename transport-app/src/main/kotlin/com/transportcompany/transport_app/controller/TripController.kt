package com.transportcompany.transport_app.controller

import com.transportcompany.transport_app.dto.CreateTripRequest
import com.transportcompany.transport_app.dto.TripResponse
import com.transportcompany.transport_app.service.TripService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime

@Validated
@RestController
@RequestMapping("/trips")
class TripController(
    private val tripService: TripService
) {

    @PostMapping
    fun createTrip(@RequestBody @Valid request: CreateTripRequest): ResponseEntity<TripResponse> {
        val response = tripService.createTrip(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @PutMapping("/{id}")
    fun updateTrip(@PathVariable id: Long, @RequestBody @Valid request: CreateTripRequest): TripResponse {
        return tripService.updateTrip(id, request)
    }

    @GetMapping
    fun getAllTrips(
        @PageableDefault(size = 25) pageable: Pageable,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) startDate: LocalDateTime?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) endDate: LocalDateTime?
    ): Page<TripResponse> {
        return tripService.getAll(pageable, startDate, endDate)
    }

    @GetMapping("/{id}")
    fun getTripById(@PathVariable id: Long): TripResponse {
        return tripService.getById(id)
    }
}