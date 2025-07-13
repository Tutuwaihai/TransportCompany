package com.transportcompany.transport_app.repository

import com.transportcompany.transport_app.model.Warehouse
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface WarehouseRepository : JpaRepository<Warehouse, Long> {
    @Query("""
        SELECT w FROM Warehouse w
        LEFT JOIN FETCH w.city
        WHERE w.isDeleted = 0 AND w.city.id = :cityId
    """)
    fun findAllByCityId(@Param("cityId") cityId: Long): List<Warehouse>
}