package com.transportcompany.transport_app.repository

import com.transportcompany.transport_app.model.Transport
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TransportRepository : JpaRepository<Transport, Long> {

    @Query("""
        SELECT t FROM Transport t
        LEFT JOIN FETCH t.city c
        LEFT JOIN FETCH t.modelTransport m
        WHERE t.isDeleted = 0
    """)
    fun findAllTransport(): List<Transport>

    fun findAllByIsDeleted(isDeleted: Int = 0, pageable: Pageable): Page<Transport>

    @Query("""
        SELECT t FROM Transport t
        LEFT JOIN FETCH t.city c
        LEFT JOIN FETCH t.modelTransport m
        WHERE t.isDeleted = 0 AND (m.type = 0 OR m.type = 1)
    """)
    fun findAllTransportWithModel(): List<Transport>

    @Query("""
        SELECT t FROM Transport t
        LEFT JOIN FETCH t.city c
        LEFT JOIN FETCH t.modelTransport m
        WHERE t.isDeleted = 0 AND (m.type = 2 OR m.type = 3)
    """)
    fun findAllTrailersWithModel(): List<Transport>

    @Query(
        value = """
            SELECT t FROM Transport t
            LEFT JOIN t.city c
            LEFT JOIN t.modelTransport m
            WHERE t.isDeleted = 0 AND (m.type = 0 OR m.type = 1)
        """,
        countQuery = """
            SELECT COUNT(t) FROM Transport t
            LEFT JOIN t.modelTransport m
            WHERE t.isDeleted = 0 AND (m.type = 0 OR m.type = 1)
        """
    )
    fun findAllTransportWithModelPaged(pageable: Pageable): Page<Transport>

    @Query(
        value = """
            SELECT t FROM Transport t
            LEFT JOIN t.city c
            LEFT JOIN t.modelTransport m
            WHERE t.isDeleted = 0 AND m IS NOT NULL AND (m.type = 2 OR m.type = 3)
            AND (:cityId IS NULL OR c.id = :cityId)
        """,
        countQuery = """
            SELECT COUNT(t) FROM Transport t
            LEFT JOIN t.modelTransport m
            LEFT JOIN t.city c
            WHERE t.isDeleted = 0 AND m IS NOT NULL AND (m.type = 2 OR m.type = 3)
            AND (:cityId IS NULL OR c.id = :cityId)
        """
    )
    fun findAllTrailersWithModelPaged(
        pageable: Pageable,
        @Param("cityId") cityId: Long?,
        @Param("number") number: String?
    ): Page<Transport>
}