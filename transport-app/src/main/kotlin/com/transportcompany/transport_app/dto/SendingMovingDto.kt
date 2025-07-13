package com.transportcompany.transport_app.dto

import java.time.LocalDateTime

data class SendingMovingDto(
    val id: Long,
    val idsending: SendingDto?
)