package com.transportcompany.transport_app.repository

import com.transportcompany.transport_app.model.TripUnion
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface TripUnionRepository : JpaRepository<TripUnion, Long> {
    
    @Query("""
        SELECT t FROM TripUnion t
        LEFT JOIN FETCH t.route
        LEFT JOIN FETCH t.transport
        LEFT JOIN FETCH t.trailer
        LEFT JOIN FETCH t.employee
        LEFT JOIN FETCH t.firmCarrier
        LEFT JOIN FETCH t.firmCustomer
        WHERE t.id = :id AND t.isDeleted = 0
    """)
    fun findByIdWithRelations(id: Long): TripUnion?

    @Query("""
        SELECT DISTINCT t FROM TripUnion t
        LEFT JOIN FETCH t.route
        LEFT JOIN FETCH t.transport tr
        LEFT JOIN FETCH tr.modelTransport
        LEFT JOIN FETCH tr.city
        LEFT JOIN FETCH t.trailer trl
        LEFT JOIN FETCH trl.modelTransport
        LEFT JOIN FETCH trl.city
        LEFT JOIN FETCH t.employee
        LEFT JOIN FETCH t.firmCarrier
        LEFT JOIN FETCH t.firmCustomer
        WHERE t.isDeleted = 0
    """)
    fun findAllWithRelations(): List<TripUnion>

    @Query("""
        SELECT t FROM TripUnion t
        LEFT JOIN t.route
        LEFT JOIN t.transport
        LEFT JOIN t.trailer
        LEFT JOIN t.employee
        LEFT JOIN t.firmCarrier
        LEFT JOIN t.firmCustomer
        WHERE t.isDeleted = 0 AND t.isActive = 1
    """, countQuery = """
        SELECT COUNT(t) FROM TripUnion t
        WHERE t.isDeleted = 0 AND t.isActive = 1
    """)
    fun findAllActiveAndNotDeleted(pageable: Pageable): Page<TripUnion>

    @Query("""
        SELECT t FROM TripUnion t
        LEFT JOIN t.route
        LEFT JOIN t.transport
        LEFT JOIN t.trailer
        LEFT JOIN t.employee
        LEFT JOIN t.firmCarrier
        LEFT JOIN t.firmCustomer
        WHERE t.isDeleted = 0 AND t.isActive = 1
        AND t.createDate >= :startDate
    """, countQuery = """
        SELECT COUNT(t) FROM TripUnion t
        WHERE t.isDeleted = 0 AND t.isActive = 1
        AND t.createDate >= :startDate
    """)
    fun findAllActiveAndNotDeletedFrom(
        pageable: Pageable,
        @Param("startDate") startDate: LocalDateTime
    ): Page<TripUnion>

    @Query("""
        SELECT t FROM TripUnion t
        LEFT JOIN t.route
        LEFT JOIN t.transport
        LEFT JOIN t.trailer
        LEFT JOIN t.employee
        LEFT JOIN t.firmCarrier
        LEFT JOIN t.firmCustomer
        WHERE t.isDeleted = 0 AND t.isActive = 1
        AND t.createDate <= :endDate
    """, countQuery = """
        SELECT COUNT(t) FROM TripUnion t
        WHERE t.isDeleted = 0 AND t.isActive = 1
        AND t.createDate <= :endDate
    """)
    fun findAllActiveAndNotDeletedTo(
        pageable: Pageable,
        @Param("endDate") endDate: LocalDateTime
    ): Page<TripUnion>

    @Query("""
        SELECT t FROM TripUnion t
        LEFT JOIN t.route
        LEFT JOIN t.transport
        LEFT JOIN t.trailer
        LEFT JOIN t.employee
        LEFT JOIN t.firmCarrier
        LEFT JOIN t.firmCustomer
        WHERE t.isDeleted = 0 AND t.isActive = 1
        AND t.createDate >= :startDate AND t.createDate <= :endDate
    """, countQuery = """
        SELECT COUNT(t) FROM TripUnion t
        WHERE t.isDeleted = 0 AND t.isActive = 1
        AND t.createDate >= :startDate AND t.createDate <= :endDate
    """)
    fun findAllActiveAndNotDeletedBetween(
        pageable: Pageable,
        @Param("startDate") startDate: LocalDateTime,
        @Param("endDate") endDate: LocalDateTime
    ): Page<TripUnion>
}