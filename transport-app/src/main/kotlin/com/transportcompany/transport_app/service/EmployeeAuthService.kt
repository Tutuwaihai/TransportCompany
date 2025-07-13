package com.transportcompany.transport_app.service


import com.transportcompany.transport_app.dto.ApiResponse
import com.transportcompany.transport_app.dto.AuthResponse
import com.transportcompany.transport_app.dto.LoginRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class EmployeeAuthService(
    private val authService: AuthService,
) {

    fun login(request: LoginRequest, devToken: String): ApiResponse<AuthResponse> {
        val response = authService.login(request, devToken)

        return ApiResponse(
            code = HttpStatus.OK.value(),
            status = HttpStatus.OK,
            message = "Login success",
            data = response
        )
    }
}