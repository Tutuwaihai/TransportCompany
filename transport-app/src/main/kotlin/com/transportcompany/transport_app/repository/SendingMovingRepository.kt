package com.transportcompany.transport_app.repository

import com.transportcompany.transport_app.model.Sending
import com.transportcompany.transport_app.model.SendingMoving
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface SendingMovingRepository : JpaRepository<SendingMoving, Long> {
    @Query("""
        SELECT COUNT(sm) 
        FROM SendingMoving sm 
        WHERE sm.typePointer = :typePointer 
        AND sm.idPointer = :idPointer
    """)
    fun countByTypePointerAndIdPointer(
        @Param("typePointer") typePointer: Int,
        @Param("idPointer") idPointer: Long
    ): Long

    @Query("""
        SELECT DISTINCT sm 
        FROM SendingMoving sm 
        LEFT JOIN FETCH sm.sending s
        LEFT JOIN FETCH s.clientFrom
        LEFT JOIN FETCH s.clientTo
        LEFT JOIN FETCH s.cityFrom
        LEFT JOIN FETCH s.cityTo
        LEFT JOIN FETCH s.price
        LEFT JOIN FETCH s.wareFrom
        LEFT JOIN FETCH s.wareTo
        WHERE sm.typePointer = :typePointer 
        AND sm.idPointer = :idPointer
    """)
    fun findAllByTypePointerAndIdPointer(
        @Param("typePointer") typePointer: Int,
        @Param("idPointer") idPointer: Long,
        pageable: Pageable
    ): List<SendingMoving>

    @Query("""
        SELECT s 
        FROM Sending s 
        WHERE s.id = :id
    """)
    fun findSendingById(@Param("id") id: Long): Sending?

    @Query("""
        SELECT DISTINCT sm 
        FROM SendingMoving sm 
        LEFT JOIN FETCH sm.sending s
        LEFT JOIN FETCH s.clientFrom
        LEFT JOIN FETCH s.clientTo
        LEFT JOIN FETCH s.cityFrom
        LEFT JOIN FETCH s.cityTo
        LEFT JOIN FETCH s.price
        LEFT JOIN FETCH s.wareFrom
        LEFT JOIN FETCH s.wareTo
        WHERE sm.typePointer = :typePointer 
        AND sm.idPointer = :idPointer
    """)
    fun findByTypePointerAndIdPointer(
        @Param("typePointer") typePointer: Int,
        @Param("idPointer") idPointer: Long
    ): List<SendingMoving>

    @Query("""
        SELECT sm FROM SendingMoving sm
        LEFT JOIN FETCH sm.sending s
        LEFT JOIN FETCH s.clientFrom
        LEFT JOIN FETCH s.clientTo
        LEFT JOIN FETCH s.cityFrom
        LEFT JOIN FETCH s.cityTo
        LEFT JOIN FETCH s.price
        LEFT JOIN FETCH s.wareFrom
        LEFT JOIN FETCH s.wareTo
        WHERE sm.typePointer = :typePointer AND sm.idPointer = :idPointer
    """)
    fun findAllFullByTypePointerAndIdPointer(
        @Param("typePointer") typePointer: Int,
        @Param("idPointer") idPointer: Long
    ): List<SendingMoving>

    @Query("""
        SELECT DISTINCT sm 
        FROM SendingMoving sm 
        LEFT JOIN FETCH sm.sending s
        LEFT JOIN FETCH s.clientFrom
        LEFT JOIN FETCH s.clientTo
        LEFT JOIN FETCH s.cityFrom
        LEFT JOIN FETCH s.cityTo
        LEFT JOIN FETCH s.price
        LEFT JOIN FETCH s.wareFrom
        LEFT JOIN FETCH s.wareTo
        WHERE sm.typePointer = :typePointer 
        AND sm.idPointer = :idPointer
        AND (:dateFrom IS NULL OR s.sendDate >= :dateFrom)
        AND (:dateTo IS NULL OR s.sendDate <= :dateTo)
    """)
    fun findAllByTypePointerAndIdPointerWithSendDate(
        @Param("typePointer") typePointer: Int,
        @Param("idPointer") idPointer: Long,
        @Param("dateFrom") dateFrom: java.time.LocalDateTime?,
        @Param("dateTo") dateTo: java.time.LocalDateTime?,
        pageable: Pageable
    ): List<SendingMoving>

    @Query("""
        SELECT COUNT(sm) 
        FROM SendingMoving sm 
        LEFT JOIN sm.sending s
        WHERE sm.typePointer = :typePointer 
        AND sm.idPointer = :idPointer
        AND (:dateFrom IS NULL OR s.sendDate >= :dateFrom)
        AND (:dateTo IS NULL OR s.sendDate <= :dateTo)
    """)
    fun countByTypePointerAndIdPointerWithSendDate(
        @Param("typePointer") typePointer: Int,
        @Param("idPointer") idPointer: Long,
        @Param("dateFrom") dateFrom: java.time.LocalDateTime?,
        @Param("dateTo") dateTo: java.time.LocalDateTime?
    ): Long

    @Query("""
        SELECT DISTINCT sm FROM SendingMoving sm
        LEFT JOIN FETCH sm.sending s
        LEFT JOIN FETCH s.clientFrom
        LEFT JOIN FETCH s.clientTo
        LEFT JOIN FETCH s.cityFrom
        LEFT JOIN FETCH s.cityTo
        LEFT JOIN FETCH s.price
        LEFT JOIN FETCH s.wareFrom
        LEFT JOIN FETCH s.wareTo
        WHERE sm.typePointer = :typePointer
          AND sm.idPointer = :idPointer
          AND s.sendDate >= :dateFrom AND s.sendDate <= :dateTo
    """)
    fun findAllByTypePointerAndIdPointerWithSendDateBetween(
        @Param("typePointer") typePointer: Int,
        @Param("idPointer") idPointer: Long,
        @Param("dateFrom") dateFrom: java.time.LocalDateTime,
        @Param("dateTo") dateTo: java.time.LocalDateTime,
        pageable: Pageable
    ): List<SendingMoving>

    @Query("""
        SELECT COUNT(sm) FROM SendingMoving sm
        LEFT JOIN sm.sending s
        WHERE sm.typePointer = :typePointer
          AND sm.idPointer = :idPointer
          AND s.sendDate >= :dateFrom AND s.sendDate <= :dateTo
    """)
    fun countByTypePointerAndIdPointerWithSendDateBetween(
        @Param("typePointer") typePointer: Int,
        @Param("idPointer") idPointer: Long,
        @Param("dateFrom") dateFrom: java.time.LocalDateTime,
        @Param("dateTo") dateTo: java.time.LocalDateTime
    ): Long

    @Query("""
        SELECT DISTINCT sm FROM SendingMoving sm
        LEFT JOIN FETCH sm.sending s
        LEFT JOIN FETCH s.clientFrom
        LEFT JOIN FETCH s.clientTo
        LEFT JOIN FETCH s.cityFrom
        LEFT JOIN FETCH s.cityTo
        LEFT JOIN FETCH s.price
        LEFT JOIN FETCH s.wareFrom
        LEFT JOIN FETCH s.wareTo
        WHERE sm.typePointer = :typePointer
          AND sm.idPointer = :idPointer
          AND s.sendDate >= :dateFrom
    """)
    fun findAllByTypePointerAndIdPointerWithSendDateFrom(
        @Param("typePointer") typePointer: Int,
        @Param("idPointer") idPointer: Long,
        @Param("dateFrom") dateFrom: java.time.LocalDateTime,
        pageable: Pageable
    ): List<SendingMoving>

    @Query("""
        SELECT COUNT(sm) FROM SendingMoving sm
        LEFT JOIN sm.sending s
        WHERE sm.typePointer = :typePointer
          AND sm.idPointer = :idPointer
          AND s.sendDate >= :dateFrom
    """)
    fun countByTypePointerAndIdPointerWithSendDateFrom(
        @Param("typePointer") typePointer: Int,
        @Param("idPointer") idPointer: Long,
        @Param("dateFrom") dateFrom: java.time.LocalDateTime
    ): Long

    @Query("""
        SELECT DISTINCT sm FROM SendingMoving sm
        LEFT JOIN FETCH sm.sending s
        LEFT JOIN FETCH s.clientFrom
        LEFT JOIN FETCH s.clientTo
        LEFT JOIN FETCH s.cityFrom
        LEFT JOIN FETCH s.cityTo
        LEFT JOIN FETCH s.price
        LEFT JOIN FETCH s.wareFrom
        LEFT JOIN FETCH s.wareTo
        WHERE sm.typePointer = :typePointer
          AND sm.idPointer = :idPointer
          AND s.sendDate <= :dateTo
    """)
    fun findAllByTypePointerAndIdPointerWithSendDateTo(
        @Param("typePointer") typePointer: Int,
        @Param("idPointer") idPointer: Long,
        @Param("dateTo") dateTo: java.time.LocalDateTime,
        pageable: Pageable
    ): List<SendingMoving>

    @Query("""
        SELECT COUNT(sm) FROM SendingMoving sm
        LEFT JOIN sm.sending s
        WHERE sm.typePointer = :typePointer
          AND sm.idPointer = :idPointer
          AND s.sendDate <= :dateTo
    """)
    fun countByTypePointerAndIdPointerWithSendDateTo(
        @Param("typePointer") typePointer: Int,
        @Param("idPointer") idPointer: Long,
        @Param("dateTo") dateTo: java.time.LocalDateTime
    ): Long
}