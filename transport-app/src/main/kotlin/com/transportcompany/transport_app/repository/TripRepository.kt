package com.transportcompany.transport_app.repository

import com.transportcompany.transport_app.model.Trip
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

@Repository
interface TripRepository : JpaRepository<Trip, Long> {
    @Query("SELECT t FROM Trip t WHERE t.isDeleted = 0")
    fun findAllTrips(pageable: Pageable): Page<Trip>

    @Query("SELECT t FROM Trip t WHERE t.isDeleted = 0 AND t.createDate BETWEEN :startDate AND :endDate")
    fun findAllTripsWithDateRange(
        @Param("startDate") startDate: LocalDateTime,
        @Param("endDate") endDate: LocalDateTime,
        pageable: Pageable
    ): Page<Trip>
}