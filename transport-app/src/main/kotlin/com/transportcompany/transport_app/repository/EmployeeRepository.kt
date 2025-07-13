package com.transportcompany.transport_app.repository

import com.transportcompany.transport_app.model.Employee
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface EmployeeRepository : JpaRepository<Employee, Long> {

    @Query("""
        SELECT e FROM Employee e
        LEFT JOIN FETCH e.city
        WHERE e.isDeleted = 0
    """)
    fun findAllDrivers(): List<Employee>

    fun findByEmail(email: String): Employee?

    fun findAllByIsDeleted(isDeleted: Int = 0, pageable: Pageable): Page<Employee>

    @Query("""
        SELECT e FROM Employee e
        LEFT JOIN FETCH e.city
        WHERE e.isDeleted = :isDeleted
        AND e.workerType IN :workerTypes
    """)
    fun findAllByIsDeletedAndWorkerTypeIn(isDeleted: Int, workerTypes: List<Long>, pageable: Pageable): Page<Employee>

    @Query("""
        SELECT e FROM Employee e
        WHERE e.isDeleted = :isDeleted
        AND (:cityId IS NULL OR e.city.id = :cityId)
        AND (:workerTypes IS NULL OR e.workerType IN :workerTypes)
    """)
    fun findAllByFilters(
        isDeleted: Int,
        cityId: Long?,
        workerTypes: List<Long>?,
        pageable: Pageable
    ): Page<Employee>

    @Query("""
        SELECT COUNT(e) FROM Employee e
        WHERE e.isDeleted = :isDeleted
        AND (:cityId IS NULL OR e.city.id = :cityId)
        AND (:workerTypes IS NULL OR e.workerType IN :workerTypes)
    """)
    fun countByFilters(
        isDeleted: Int,
        cityId: Long?,
        workerTypes: List<Long>?
    ): Long
}