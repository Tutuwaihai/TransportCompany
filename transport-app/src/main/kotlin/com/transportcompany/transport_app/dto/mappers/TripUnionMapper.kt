package com.transportcompany.transport_app.dto.mappers

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.springframework.beans.factory.annotation.Autowired
import com.transportcompany.transport_app.dto.TripUnionRequest
import com.transportcompany.transport_app.dto.TripUnionResponse
import com.transportcompany.transport_app.model.TripUnion
import com.transportcompany.transport_app.repository.RouteRepository
import com.transportcompany.transport_app.repository.TransportRepository
import com.transportcompany.transport_app.repository.EmployeeRepository
import com.transportcompany.transport_app.repository.FirmRepository
import java.time.LocalDateTime
import jakarta.persistence.EntityNotFoundException

@Mapper(componentModel = "spring", uses = [TransportMapper::class, FirmMapper::class])
abstract class TripUnionMapper {

    @Autowired
    protected lateinit var routeRepository: RouteRepository

    @Autowired
    protected lateinit var transportRepository: TransportRepository

    @Autowired
    protected lateinit var employeeRepository: EmployeeRepository

    @Autowired
    protected lateinit var firmRepository: FirmRepository

    @Autowired
    protected lateinit var transportMapper: TransportMapper

    @Autowired
    protected lateinit var firmMapper: FirmMapper

    @Autowired
    protected lateinit var routeMapper: RouteMapper

    @Autowired
    protected lateinit var employeeMapper: EmployeeMapper

    @Mapping(target = "route", ignore = false)
    @Mapping(target = "employee", ignore = false)
    @Mapping(target = "transport", expression = "java(entity.getTransport() != null ? transportMapper.toDto(entity.getTransport()) : null)")
    @Mapping(target = "trailer", expression = "java(mapTrailer(entity.getTrailer()))")
    fun toResponse(entity: TripUnion): TripUnionResponse {
        val firmCarrierDto = if (entity.firmCarrier != null && entity.firmCarrier!!.id != 0L) {
            firmMapper.toDtoList(listOf(entity.firmCarrier)).firstOrNull()
        } else null

        val firmCustomerDto = if (entity.firmCustomer != null && entity.firmCustomer!!.id != 0L) {
            firmMapper.toDtoList(listOf(entity.firmCustomer)).firstOrNull()
        } else null

        val routeDto = entity.route?.let { routeMapper.toDtoList(listOf(it)).firstOrNull() }
        val employeeDto = entity.employee?.let { employeeMapper.toDtoList(listOf(it)).firstOrNull() }

        return TripUnionResponse(
            id = entity.id,
            createDate = entity.createDate,
            modifyDate = entity.modifyDate,
            firmCustomer = firmCustomerDto,
            firmCarrier = firmCarrierDto,
            route = routeDto,
            employee = employeeDto,
            transport = entity.transport?.let { transportMapper.toDto(it) },
            trailer = mapTrailer(entity.trailer),
            costs = entity.costs,
            description = entity.description
        )
    }

    abstract fun toList(entity: List<TripUnion>): List<TripUnionResponse>

    fun toEntity(dto: TripUnionRequest, userId: Long): TripUnion {
        val route = dto.idRoute?.let { 
            routeRepository.findById(it).orElseThrow { 
                EntityNotFoundException("Маршрут с id=$it не найден") 
            }
        }
        
        val transport = dto.idTransport?.let { 
            transportRepository.findById(it).orElseThrow { 
                EntityNotFoundException("Транспорт с id=$it не найден") 
            }
        }
        
        val trailer = if (dto.idTrailer == null || dto.idTrailer == -1L) null else dto.idTrailer.let { 
            transportRepository.findById(it).orElseThrow { 
                EntityNotFoundException("Прицеп с id=$it не найден")
            }
        }
        
        val employee = dto.idEmployee?.let { 
            employeeRepository.findById(it).orElseThrow { 
                EntityNotFoundException("Водитель с id=$it не найден") 
            }
        }
        
        val firmCarrier = when {
            dto.idFirmCarrier == null || dto.idFirmCarrier == 0L -> null
            else -> firmRepository.findById(dto.idFirmCarrier).orElseThrow { 
                EntityNotFoundException("Фирма-перевозчик с id=${dto.idFirmCarrier} не найдена") 
            }
        }
        
        val firmCustomer = when {
            dto.idFirmCustomer == null || dto.idFirmCustomer == 0L -> null
            else -> firmRepository.findById(dto.idFirmCustomer).orElseThrow { 
                EntityNotFoundException("Фирма-заказчик с id=${dto.idFirmCustomer} не найдена") 
            }
        }

        return TripUnion(
            createDate = LocalDateTime.now(),
            modifyDate = LocalDateTime.now(),
            createUser = userId,
            modifyUser = null,
            isDeleted = 0,
            isActive = 1,
            route = route,
            transport = transport,
            trailer = trailer,
            employee = employee,
            firmCarrier = firmCarrier,
            firmCustomer = firmCustomer,
            costs = dto.costs,
            description = dto.description
        )
    }

    fun updateTripUnionFromRequest(dto: TripUnionRequest, entity: TripUnion, userId: Long): TripUnion {
        val route = dto.idRoute?.let { 
            routeRepository.findById(it).orElseThrow { 
                EntityNotFoundException("Маршрут с id=$it не найден") 
            }
        } ?: entity.route

        val transport = dto.idTransport?.let { 
            transportRepository.findById(it).orElseThrow { 
                EntityNotFoundException("Транспорт с id=$it не найден") 
            }
        } ?: entity.transport

        val trailer = when {
            dto.idTrailer == null || dto.idTrailer == -1L -> null
            dto.idTrailer != null -> transportRepository.findById(dto.idTrailer).orElseThrow {
                EntityNotFoundException("Прицеп с id=${dto.idTrailer} не найден")
            }
            else -> entity.trailer
        }

        val employee = dto.idEmployee?.let { 
            employeeRepository.findById(it).orElseThrow { 
                EntityNotFoundException("Водитель с id=$it не найден") 
            }
        } ?: entity.employee

        val firmCarrier = when {
            dto.idFirmCarrier == null || dto.idFirmCarrier == 0L -> entity.firmCarrier
            else -> firmRepository.findById(dto.idFirmCarrier).orElseThrow { 
                EntityNotFoundException("Фирма-перевозчик с id=${dto.idFirmCarrier} не найдена") 
            }
        }

        val firmCustomer = when {
            dto.idFirmCustomer == null || dto.idFirmCustomer == 0L -> entity.firmCustomer
            else -> firmRepository.findById(dto.idFirmCustomer).orElseThrow { 
                EntityNotFoundException("Фирма-заказчик с id=${dto.idFirmCustomer} не найдена") 
            }
        }

        return entity.copy(
            modifyDate = LocalDateTime.now(),
            modifyUser = userId,
            route = route,
            transport = transport,
            trailer = trailer,
            employee = employee,
            firmCarrier = firmCarrier,
            firmCustomer = firmCustomer,
            costs = dto.costs ?: entity.costs,
            description = dto.description ?: entity.description
        )
    }

    fun mapTrailer(trailer: com.transportcompany.transport_app.model.Transport?): com.transportcompany.transport_app.dto.TransportDto? {
        return when {
            trailer == null -> null
            trailer.id == -1L -> com.transportcompany.transport_app.dto.TransportDto(
                id = -1L,
                model = "нет прицепа",
                licensePlate = null,
                tonnage = null,
                cityTitle = null,
                modelType = null,
                ownerType = null
            )
            else -> transportMapper.toDto(trailer)
        }
    }
}
