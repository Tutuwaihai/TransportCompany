package com.transportcompany.transport_app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TransportAppApplication

fun main(args: Array<String>) {
	runApplication<TransportAppApplication>(*args)
}
