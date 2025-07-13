package com.transportcompany.transport_app.exception

import com.transportcompany.transport_app.dto.ApiResponse
import jakarta.persistence.EntityNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.format.DateTimeParseException

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFoundException(ex: EntityNotFoundException): ResponseEntity<ApiResponse<Nothing>> {
        val response = ApiResponse(
            code = HttpStatus.NOT_FOUND.value(),
            status = HttpStatus.NOT_FOUND,
            message = ex.message ?: "Запрашиваемый ресурс не найден",
            data = null
        )
        return ResponseEntity(response, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalStateException(ex: IllegalStateException): ResponseEntity<ApiResponse<Nothing>> {
        val status = when {
            ex.message?.contains("authenticated", ignoreCase = true) == true -> HttpStatus.UNAUTHORIZED
            else -> HttpStatus.BAD_REQUEST
        }
        
        val response = ApiResponse(
            code = status.value(),
            status = status,
            message = ex.message ?: "Ошибка в состоянии приложения",
            data = null
        )
        return ResponseEntity(response, status)
    }

    @ExceptionHandler(InvalidJwtTokenException::class)
    fun handleInvalidJwtTokenException(ex: InvalidJwtTokenException): ResponseEntity<ApiResponse<Nothing>> {
        val response = ApiResponse(
            code = HttpStatus.UNAUTHORIZED.value(),
            status = HttpStatus.UNAUTHORIZED,
            message = ex.message ?: "Недействительный токен авторизации",
            data = null
        )
        return ResponseEntity(response, HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(ex: AccessDeniedException): ResponseEntity<ApiResponse<Nothing>> {
        val response = ApiResponse(
            code = HttpStatus.FORBIDDEN.value(),
            status = HttpStatus.FORBIDDEN,
            message = "Отказано в доступе",
            data = null
        )
        return ResponseEntity(response, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<ApiResponse<Map<String, String>>> {
        val errors = ex.bindingResult.fieldErrors.associate { 
            it.field to (it.defaultMessage ?: "Ошибка валидации")
        }
        
        val response = ApiResponse(
            code = HttpStatus.BAD_REQUEST.value(),
            status = HttpStatus.BAD_REQUEST,
            message = "Ошибка валидации данных",
            data = errors
        )
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(DateTimeParseException::class)
    fun handleDateTimeParseException(ex: DateTimeParseException): ResponseEntity<ApiResponse<Nothing>> {
        val response = ApiResponse(
            code = HttpStatus.BAD_REQUEST.value(),
            status = HttpStatus.BAD_REQUEST,
            message = "Неверный формат даты/времени",
            data = null
        )
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<ApiResponse<Nothing>> {
        val response = ApiResponse(
            code = HttpStatus.BAD_REQUEST.value(),
            status = HttpStatus.BAD_REQUEST,
            message = ex.message ?: "Некорректный аргумент запроса",
            data = null
        )
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ApiResponse<Nothing>> {
        val response = ApiResponse(
            code = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            status = HttpStatus.INTERNAL_SERVER_ERROR,
            message = "Внутренняя ошибка сервера",
            data = null
        )
        return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}