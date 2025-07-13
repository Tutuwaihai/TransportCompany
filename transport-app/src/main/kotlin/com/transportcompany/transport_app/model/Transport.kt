package com.transportcompany.transport_app.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "new_transport")
data class Transport(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "modifydate")
    val modifyDate: LocalDateTime? = null,

    @Column(name = "createdate")
    val createDate: LocalDateTime? = null,

    @Column(name = "isdeleted")
    val isDeleted: Int = 0,

    @Column(name = "license_plate", columnDefinition = "text")
    val licensePlate: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idcity")
    val city: City? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idmodel")
    val modelTransport: ModelTransport? = null,

    @Column(name = "type")
    val type: Int? = null // только для прицепа, для машины брать из modelTransport
)
