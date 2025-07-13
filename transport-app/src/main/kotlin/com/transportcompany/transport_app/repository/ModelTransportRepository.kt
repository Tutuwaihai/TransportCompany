package com.transportcompany.transport_app.repository

import com.transportcompany.transport_app.model.ModelTransport
import com.transportcompany.transport_app.model.Transport
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ModelTransportRepository : JpaRepository<ModelTransport, Long> {

    @Query(""" 
        SELECT mt 
        FROM ModelTransport mt 
        WHERE mt.isdeleted = 0 AND (mt.type = 2 OR mt.type = 3) 
        """)
    fun findAllTrailers(): List<ModelTransport>

    fun findAllByIsdeletedAndTypeIn(isdeleted: Int = 0, type: List<Int>, pageable: Pageable): Page<ModelTransport>
}