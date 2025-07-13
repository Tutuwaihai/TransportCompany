package com.transportcompany.transport_app.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "city")
data class City(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "modifydate")
    val modifyDate: LocalDateTime? = null,

    @Column(name = "createdate")
    val createdate: LocalDateTime? = null,

    @Column(name = "isdeleted")
    val isDeleted: Int,

    @Column(name = "title")
    val title: String,

    @Column(name = "description")
    val description: String? = null
)
