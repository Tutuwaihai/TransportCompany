package com.transportcompany.transport_app.dto

import org.springframework.http.HttpStatus

data class ApiResponse<T>(
    val code: Int,
    val status: HttpStatus,
    val message: String,
    val data: T? = null
)
