package com.transportcompany.transport_app.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "warehouse")
data class Warehouse(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(name = "title")
    val title: String,
    @Column(name = "description")
    val description: String?,
    @ManyToOne
    @JoinColumn(name = "idcity")
    val city: City? = null,
    @Column(name = "address")
    val address: String? = null,
    @Column(name = "phone")
    val phone: String? = null,
    @Column(name = "email")
    val email: String? = null,
    @Column(name = "code")
    val code: String? = null,
    @Column(name = "is_default")
    val isDefault: Int? = null,
    @Column(name = "max_weight")
    val maxWeight: Float? = null,
    @Column(name = "max_height")
    val max_height: Float? = null,
    @Column(name = "modifydate")
    val modifydate: LocalDateTime? = null,
    @Column(name = "createdate")
    val createdate: LocalDateTime? = null,
    @Column(name = "isdeleted")
    val isDeleted: Int? = null
)
