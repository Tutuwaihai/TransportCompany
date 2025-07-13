package com.transportcompany.transport_app.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "model_transport")
data class ModelTransport(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @Column(name = "modifydate")
    val modifydate: LocalDateTime?,
    @Column(name = "createdate")
    val createdate: LocalDateTime?,
    @Column(name = "isdeleted")
    val isdeleted: Int?,
    @Column(name = "model")
    val model: String?,
    @Column(name = "tonnage")
    val tonnage: Double,
    @Column(name = "volume")
    val volume: Double,
    @Column(name = "width")
    val width: Double,
    @Column(name = "height")
    val height: Double,
    @Column(name = "length")
    val length: Double,
    @Column(name = "description")
    val description: String?,
    @Column(name = "type")
    val type: Int?
)
