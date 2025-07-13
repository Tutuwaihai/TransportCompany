package com.transportcompany.transport_app.repository

import com.transportcompany.transport_app.model.Route
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.data.repository.query.Param

@Repository
interface RouteRepository : JpaRepository<Route, Long> {
    fun findAllByIsDeleted(isDeleted: Int = 0): List<Route>
    fun findAllByIsDeleted(isDeleted: Int = 0, pageable: Pageable): Page<Route>
    @Query("""
        SELECT r FROM Route r
        WHERE r.isDeleted = 0
          AND (:fromCityId IS NULL OR r.idCityFrom = :fromCityId)
          AND (:toCityId IS NULL OR r.idCityTo = :toCityId)
    """)
    fun findFiltered(
        @Param("fromCityId") fromCityId: Long?,
        @Param("toCityId") toCityId: Long?,
        pageable: Pageable
    ): Page<Route>
}