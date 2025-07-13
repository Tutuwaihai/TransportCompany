package com.transportcompany.transport_app.dto

data class AuthResponse(
    val token: String,
    val accountId: String,
    val type: Int,
    val isPhoneVerified: Boolean,
    val isEmailVerified: Boolean,
)