package com.transportcompany.transport_app.controller

import com.transportcompany.transport_app.dto.ApiResponse
import com.transportcompany.transport_app.dto.AuthResponse
import com.transportcompany.transport_app.dto.LoginRequest
import com.transportcompany.transport_app.service.EmployeeAuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: EmployeeAuthService
) {
    @PostMapping("/login")
    fun login(
        @RequestHeader("NrgApi-DevToken") devToken: String,
        @RequestBody request: LoginRequest
    ): ResponseEntity<ApiResponse<AuthResponse>> {
        return ResponseEntity.ok(authService.login(request, devToken))
    }
}