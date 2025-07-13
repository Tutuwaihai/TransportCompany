package com.transportcompany.transport_app.controller


import com.transportcompany.transport_app.dto.TripUnionResponse
import com.transportcompany.transport_app.dto.TripUnionRequest
import com.transportcompany.transport_app.model.TripUnion
import com.transportcompany.transport_app.service.TripUnionService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@Validated
@RestController
@RequestMapping("/trip-union")
class TripUnionController(
    private val tripUnionService: TripUnionService
) {

   @PostMapping 
   @ResponseStatus(HttpStatus.CREATED) 
    fun createTrip(
        @RequestBody @Valid request: TripUnionRequest
    ): TripUnionResponse = tripUnionService.createTrip(request)

    @PutMapping("/{id}") 
    fun updateTrip( 
        @PathVariable id: Long,
        @RequestBody @Valid request: TripUnionRequest
    ): TripUnionResponse = tripUnionService.updateTrip(id, request)

    @GetMapping("/{id}") 
    fun getTripById(
        @PathVariable id: Long
    ): TripUnionResponse = tripUnionService.getTripUnionById(id)

    @GetMapping
    fun getAllTripUnions(
        @PageableDefault(size = 25) pageable: Pageable,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) startDate: LocalDateTime?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) endDate: LocalDateTime?
    ): Page<TripUnionResponse> = tripUnionService.getAllTripUnions(pageable, startDate, endDate)

}
