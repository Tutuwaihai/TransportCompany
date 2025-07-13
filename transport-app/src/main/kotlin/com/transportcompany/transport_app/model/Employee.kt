package com.transportcompany.transport_app.model

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "employee")
data class Employee(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(name = "modifydate")
    val modifydate: LocalDateTime? = null,
    @Column(name = "createdate")
    val createdate: LocalDateTime? = null,
    @Column(name = "isdeleted")
    val isDeleted: Int = 0,
    @Column(name = "fio", columnDefinition = "varchar")
    val fio: String,
    @Column(name = "phone")
    val phone: String? = null,
    @Column(name = "worker_type")
    val workerType: Int = 0,
    @Column(name = "passport")
    val passport: String? = null,
    @Column(name = "email")
    val email: String? = null,
    @Column(name = "description")
    val description: String? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idcity")
    val city: City? = null,
    @Column(name = "birthdate")
    val birthdate: LocalDate? = null,
    @Column(name = "passport_series")
    val passportSeries: String? = null,
    @Column(name = "passport_number")
    val passportNumber: String? = null,
    @Column(name = "address")
    val address: String? = null
)