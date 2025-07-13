package com.transportcompany.transport_app.client

import com.transportcompany.transport_app.dto.AuthResponse
import com.transportcompany.transport_app.dto.LoginRequest
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader


@FeignClient(name = "authClient", url = "\${auth.client.url}")
interface AuthClient  {

    @PostMapping("/auth/login")
    fun login(
        @RequestHeader("NrgApi-DevToken") devToken: String,
        @RequestBody request: LoginRequest
    ): AuthResponse
}
