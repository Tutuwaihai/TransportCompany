package com.transportcompany.transport_app.repository

import com.transportcompany.transport_app.model.Firm
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FirmRepository : JpaRepository<Firm, Long> {
    fun findAllByIsDeleted(isDeleted: Int = 0): List<Firm>
    fun findAllByIsDeleted(isDeleted: Int = 0, pageable: Pageable): Page<Firm>
}