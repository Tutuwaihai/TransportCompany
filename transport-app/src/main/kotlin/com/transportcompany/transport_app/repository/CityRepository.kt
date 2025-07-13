package com.transportcompany.transport_app.repository

import com.transportcompany.transport_app.model.City
import org.springframework.data.jpa.repository.JpaRepository

interface CityRepository : JpaRepository<City, Long> {
    fun findAllByIsDeleted(isDeleted: Int = 0): List<City>
}