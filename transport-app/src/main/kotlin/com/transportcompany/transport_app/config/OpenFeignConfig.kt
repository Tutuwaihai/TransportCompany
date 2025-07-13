package com.transportcompany.transport_app.config

import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration

@EnableFeignClients(basePackages = ["com.transportcompany.transport_app"])
@Configuration
class OpenFeignConfig {

}