package com.transportcompany.transport_app.repository

import com.transportcompany.transport_app.model.TripType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface TripTypeRepository : JpaRepository<TripType, Long> {
    @Query("""
        SELECT t FROM TripType t
        WHERE t.isDeleted = 0
    """)
    fun findAllActive(): List<TripType>
}