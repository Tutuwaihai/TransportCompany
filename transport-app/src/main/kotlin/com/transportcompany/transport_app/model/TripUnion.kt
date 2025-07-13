package com.transportcompany.transport_app.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "trip_union")
data class TripUnion(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "modifydate")
    var modifyDate: LocalDateTime? = null,

    @Column(name = "createdate")
    val createDate: LocalDateTime = LocalDateTime.now(),

    @Column(name = "createuser")
    val createUser: Long? = null,

    @Column(name = "modifyuser")
    val modifyUser: Long? = null,

    @Column(name = "isdeleted")
    val isDeleted: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idroute")
    var route: Route?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idtransport")
    var transport: Transport?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idtrailer")
    var trailer: Transport? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idemployee")
    var employee: Employee?,

    @Column(name = "costs")
    var costs: BigDecimal? = null,

    @Column(name = "description")
    var description: String? = null,

    @Column(name = "is_active")
    var isActive: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_firm_carrier")
    var firmCarrier: Firm?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_firm_customer")
    var firmCustomer: Firm?
)
