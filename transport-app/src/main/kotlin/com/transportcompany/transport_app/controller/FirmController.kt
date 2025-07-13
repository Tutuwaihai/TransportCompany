package com.transportcompany.transport_app.controller

import com.transportcompany.transport_app.dto.FirmDto
import com.transportcompany.transport_app.service.FirmService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/firms")
class FirmController(
    private val firmService: FirmService
) {
    @GetMapping
    fun getAllFirms(): List<FirmDto> = firmService.getAllFirms()

    @GetMapping("/page")
    fun getFirmsPage(pageable: Pageable): Page<FirmDto> = firmService.getFirmsPage(pageable)
}