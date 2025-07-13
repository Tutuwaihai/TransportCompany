package com.transportcompany.transport_app.service

import com.transportcompany.transport_app.client.AuthClient
import com.transportcompany.transport_app.dto.AuthResponse
import com.transportcompany.transport_app.dto.LoginRequest
import org.springframework.stereotype.Service



@Service
class AuthService(
    private val authClient: AuthClient
) {
    fun login(request: LoginRequest, devToken: String): AuthResponse {
        return authClient.login(devToken, request)

    }
}