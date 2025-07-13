package com.transportcompany.transport_app.dto.mappers

import com.transportcompany.transport_app.dto.*
import com.transportcompany.transport_app.model.City
import com.transportcompany.transport_app.model.Trip
import com.transportcompany.transport_app.model.TripType
import com.transportcompany.transport_app.model.Warehouse
import org.mapstruct.*
import org.springframework.beans.factory.annotation.Autowired
import com.transportcompany.transport_app.repository.*
import java.math.BigDecimal
import java.time.LocalDateTime
import jakarta.persistence.EntityNotFoundException

@Mapper(componentModel = "spring", uses = [EmployeeMapper::class, TransportMapper::class])
abstract class TripMapper {
    @Autowired
    protected lateinit var cityRepository: CityRepository

    @Autowired
    protected lateinit var warehouseRepository: WarehouseRepository

    @Autowired
    protected lateinit var tripTypeRepository: TripTypeRepository

    @Autowired
    protected lateinit var employeeRepository: EmployeeRepository

    @Autowired
    protected lateinit var transportRepository: TransportRepository

    @Autowired
    protected lateinit var tripStateRepository: TripStateRepository

    @Autowired
    protected lateinit var employeeMapper: EmployeeMapper

    @Autowired
    protected lateinit var transportMapper: TransportMapper

    @Autowired
    protected lateinit var tripStateMapper: TripStateMapper

    @Autowired
    protected lateinit var tripUnionRepository: TripUnionRepository

    fun toEntity(dto: CreateTripRequest, userId: Long): Trip {
        val cityFrom = dto.cityFromId?.let {
            cityRepository.findById(it).orElseThrow {
                EntityNotFoundException("Город отправления с id=$it не найден")
            }
        } ?: throw IllegalArgumentException("ID города отправления обязателен")

        val cityTo = dto.cityToId?.let {
            cityRepository.findById(it).orElseThrow {
                EntityNotFoundException("Город прибытия с id=$it не найден")
            }
        } ?: throw IllegalArgumentException("ID города прибытия обязателен")

        val wareHouseFrom = if (dto.warehouseFromId != null && dto.warehouseFromId > 0) {
            warehouseRepository.findById(dto.warehouseFromId).orElseThrow {
                EntityNotFoundException("Склад отправления с id=${dto.warehouseFromId} не найден")
            }
        } else null

        val wareHouseTo = if (dto.warehouseToId != null && dto.warehouseToId > 0) {
            warehouseRepository.findById(dto.warehouseToId).orElseThrow {
                EntityNotFoundException("Склад прибытия с id=${dto.warehouseToId} не найден")
            }
        } else null

        val tripType = dto.tripTypeId?.let {
            tripTypeRepository.findById(it).orElseThrow {
                EntityNotFoundException("Тип рейса с id=$it не найден")
            }
        } ?: throw IllegalArgumentException("Тип рейса обязателен")

        return Trip(
            description = dto.description,
            cityFrom = cityFrom,
            cityTo = cityTo,
            wareHouseFrom = wareHouseFrom,
            wareHouseTo = wareHouseTo,
            tripType = tripType,
            dispatchDate = dto.dispatchDate,
            arrivalDate = dto.arrivalDate,
            expectedDate = dto.expectedDate,
            docNum = dto.docNum ?: "",
            isTransit = dto.isTransit,
            state = dto.state?.toIntOrNull() ?: 0,
            costs = dto.costs ?: BigDecimal.ZERO,
            isCityCosts = dto.isCityCosts,
            idEmployee = dto.employeeId,
            idTransport = dto.transportId,
            cargoSeal = dto.cargoSeal,
            createDate = LocalDateTime.now(),
            modifyDate = null,
            createUser = userId,
            modifyUser = null,
            isDeleted = 0
        )
    }

    fun updateTripFromRequest(dto: CreateTripRequest, entity: Trip, userId: Long): Trip {
        val cityFrom = dto.cityFromId?.let {
            cityRepository.findById(it).orElseThrow {
                EntityNotFoundException("Город отправления с id=$it не найден")
            }
        } ?: entity.cityFrom

        val cityTo = dto.cityToId?.let {
            cityRepository.findById(it).orElseThrow {
                EntityNotFoundException("Город прибытия с id=$it не найден")
            }
        } ?: entity.cityTo

        val wareHouseFrom = if (dto.warehouseFromId != null && dto.warehouseFromId > 0) {
            warehouseRepository.findById(dto.warehouseFromId).orElseThrow {
                EntityNotFoundException("Склад отправления с id=${dto.warehouseFromId} не найден")
            }
        } else null

        val wareHouseTo = if (dto.warehouseToId != null && dto.warehouseToId > 0) {
            warehouseRepository.findById(dto.warehouseToId).orElseThrow {
                EntityNotFoundException("Склад прибытия с id=${dto.warehouseToId} не найден")
            }
        } else null

        val tripType = dto.tripTypeId?.let {
            tripTypeRepository.findById(it).orElseThrow {
                EntityNotFoundException("Тип рейса с id=$it не найден")
            }
        } ?: entity.tripType

        return entity.copy(
            description = dto.description ?: entity.description,
            cityFrom = cityFrom,
            cityTo = cityTo,
            wareHouseFrom = wareHouseFrom,
            wareHouseTo = wareHouseTo,
            tripType = tripType,
            dispatchDate = dto.dispatchDate ?: entity.dispatchDate,
            arrivalDate = dto.arrivalDate ?: entity.arrivalDate,
            expectedDate = dto.expectedDate ?: entity.expectedDate,
            docNum = dto.docNum ?: entity.docNum,
            isTransit = dto.isTransit,
            state = dto.state?.toIntOrNull() ?: entity.state,
            costs = dto.costs ?: entity.costs,
            isCityCosts = dto.isCityCosts,
            idEmployee = dto.employeeId,
            idTransport = dto.transportId,
            cargoSeal = dto.cargoSeal ?: entity.cargoSeal,
            modifyDate = LocalDateTime.now(),
            modifyUser = userId
        )
    }

    @Mapping(target = "employee", expression = "java(mapEmployee(entity.getIdEmployee()))")
    @Mapping(target = "transport", expression = "java(mapTransport(entity.getIdTransport()))")
    @Mapping(target = "state", expression = "java(mapTripState(entity.getState()))")
    @Mapping(target = "wareHouseFrom", expression = "java(mapWarehouse(entity.getWareHouseFrom()))")
    @Mapping(target = "wareHouseTo", expression = "java(mapWarehouse(entity.getWareHouseTo()))")
    abstract fun toResponse(entity: Trip): TripResponse

    protected fun mapEmployee(employeeId: Long?): EmployeeDto? {
        if (employeeId == null) return null
        return employeeRepository.findById(employeeId)
            .map { employeeMapper.toDto(it) }
            .orElse(null)
    }

    protected fun mapTransport(transportId: Long?): TransportDto? {
        if (transportId == null) return null
        val tripUnion = tripUnionRepository.findByIdWithRelations(transportId) ?: return null
        val transport = tripUnion.transport ?: return null
        return transportMapper.toDto(transport)
    }

    protected fun mapTripState(stateId: Int): TripStateDto? {
        return tripStateRepository.findById(stateId.toLong())
            .map { tripStateMapper.toDto(it) }
            .orElse(null)
    }

    protected fun mapWarehouse(warehouse: Warehouse?): WarehouseDto? {
        if (warehouse == null || warehouse.id == 0L) return null
        return WarehouseDto(warehouse.id, warehouse.title)
    }
}