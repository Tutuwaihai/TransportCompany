package com.transportcompany.transport_app.model

import jakarta.persistence.*

@Entity
@Table(name = "client")
data class Client(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "title")
    val title: String? = null
)