package com.transportcompany.transport_app.service

import com.transportcompany.transport_app.dto.SendingMovingDto
import com.transportcompany.transport_app.dto.mappers.SendingMovingMapper
import com.transportcompany.transport_app.repository.SendingMovingRepository
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SendingMovingService(
    private val repository: SendingMovingRepository,
    private val sendingMovingMapper: SendingMovingMapper
) {
    fun getInvoicesInTrip(tripId: Long): List<SendingMovingDto> {
        val entities = repository.findAllByTypePointerAndIdPointer(2, tripId, Pageable.unpaged())
        return sendingMovingMapper.toDtoList(entities)
    }

    fun getInvoicesInWarehouse(warehouseId: Long): List<SendingMovingDto> {
        if (warehouseId == 0L) throw IllegalArgumentException("ID склада обязателен")
        val entities = repository.findAllByTypePointerAndIdPointer(1, warehouseId, Pageable.unpaged())
        entities.forEach { entity ->
        }
        return sendingMovingMapper.toDtoList(entities)
    }

    @Transactional
    fun moveToTrip(sendingIds: List<Long>, tripId: Long) {
        val entities = repository.findAllById(sendingIds)
        if (entities.size != sendingIds.size) {
            throw EntityNotFoundException("Некоторые накладные не найдены")
        }

        entities.forEach {
            it.typePointer = 2
            it.idPointer = tripId
        }

        repository.saveAll(entities)
    }

    @Transactional
    fun moveToWarehouse(sendingIds: List<Long>, warehouseId: Long) {
        if (warehouseId == 0L) throw IllegalArgumentException("ID склада обязателен")
        val entities = repository.findAllById(sendingIds)
        if (entities.size != sendingIds.size) {
            throw EntityNotFoundException("Некоторые накладные не найдены")
        }

        entities.forEach {
            it.typePointer = 1
            it.idPointer = warehouseId
        }

        repository.saveAll(entities)
    }

    @Transactional
    fun moveAllFromWarehouseToTrip(warehouseId: Long, tripId: Long) {
        if (warehouseId == 0L) throw IllegalArgumentException("ID склада обязателен")
        val sendings = repository.findByTypePointerAndIdPointer(1, warehouseId)
        sendings.forEach {
            it.typePointer = 2
            it.idPointer = tripId
        }
        repository.saveAll(sendings)
    }

    @Transactional
    fun moveAllFromTripToWarehouse(tripId: Long, warehouseId: Long) {
        if (warehouseId == 0L) throw IllegalArgumentException("ID склада обязателен")
        val sendings = repository.findByTypePointerAndIdPointer(2, tripId)
        sendings.forEach {
            it.typePointer = 1
            it.idPointer = warehouseId
        }
        repository.saveAll(sendings)
    }

    fun getFilteredSendingMovingsByWarehouse(warehouseId: Long, filter: String?): List<SendingMovingDto> {
        if (warehouseId == 0L) throw IllegalArgumentException("ID склада обязателен")
        val entities = when (filter?.uppercase()) {
            "ARRIVED" -> repository.findByTypePointerAndIdPointer(20, warehouseId)
            else -> repository.findByTypePointerAndIdPointer(1, warehouseId)
        }
        entities.forEach { entity ->
        }
        return sendingMovingMapper.toDtoList(entities)
    }

    fun getInvoicesInWarehousePaged(
        warehouseId: Long,
        dateFrom: String?,
        dateTo: String?,
        pageable: Pageable
    ): Page<SendingMovingDto> {
        if (warehouseId == 0L) throw IllegalArgumentException("ID склада обязателен")
        val from = dateFrom?.let { LocalDateTime.parse(it + "T00:00:00") }
        val to = dateTo?.let { LocalDateTime.parse(it + "T23:59:59") }
        val (entities, total) = when {
            from != null && to != null -> {
                repository.findAllByTypePointerAndIdPointerWithSendDateBetween(1, warehouseId, from, to, pageable) to
                repository.countByTypePointerAndIdPointerWithSendDateBetween(1, warehouseId, from, to)
            }
            from != null -> {
                repository.findAllByTypePointerAndIdPointerWithSendDateFrom(1, warehouseId, from, pageable) to
                repository.countByTypePointerAndIdPointerWithSendDateFrom(1, warehouseId, from)
            }
            to != null -> {
                repository.findAllByTypePointerAndIdPointerWithSendDateTo(1, warehouseId, to, pageable) to
                repository.countByTypePointerAndIdPointerWithSendDateTo(1, warehouseId, to)
            }
            else -> {
                repository.findAllByTypePointerAndIdPointer(1, warehouseId, pageable) to
                repository.countByTypePointerAndIdPointer(1, warehouseId)
            }
        }
        val dtos = sendingMovingMapper.toDtoList(entities)
        return PageImpl(dtos, pageable, total)
    }

    fun getInvoicesInTripPaged(
        tripId: Long,
        dateFrom: String?,
        dateTo: String?,
        pageable: Pageable
    ): Page<SendingMovingDto> {
        val from = dateFrom?.let { LocalDateTime.parse(it + "T00:00:00") }
        val to = dateTo?.let { LocalDateTime.parse(it + "T23:59:59") }
        val (entities, total) = when {
            from != null && to != null -> {
                repository.findAllByTypePointerAndIdPointerWithSendDateBetween(2, tripId, from, to, pageable) to
                repository.countByTypePointerAndIdPointerWithSendDateBetween(2, tripId, from, to)
            }
            from != null -> {
                repository.findAllByTypePointerAndIdPointerWithSendDateFrom(2, tripId, from, pageable) to
                repository.countByTypePointerAndIdPointerWithSendDateFrom(2, tripId, from)
            }
            to != null -> {
                repository.findAllByTypePointerAndIdPointerWithSendDateTo(2, tripId, to, pageable) to
                repository.countByTypePointerAndIdPointerWithSendDateTo(2, tripId, to)
            }
            else -> {
                repository.findAllByTypePointerAndIdPointer(2, tripId, pageable) to
                repository.countByTypePointerAndIdPointer(2, tripId)
            }
        }
        val dtos = sendingMovingMapper.toDtoList(entities)
        return PageImpl(dtos, pageable, total)
    }
}