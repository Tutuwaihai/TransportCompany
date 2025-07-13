package com.transportcompany.transport_app.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "trip_type")
data class TripType(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(name = "title")
    val title: String? = null,
    @Column(name = "description")
    val description: String? = null,
    @Column(name = "createdate")
    val createdate: LocalDateTime = LocalDateTime.now(),
    @Column(name = "modifydate")
    val modifydate: LocalDateTime? = null,
    @Column(name = "isdeleted")
    val isDeleted: Int? = null
)
