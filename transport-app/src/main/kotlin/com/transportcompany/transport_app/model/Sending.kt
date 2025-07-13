package com.transportcompany.transport_app.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "sending")
data class Sending(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "issuedate")
    val issueDate: LocalDateTime? = null,

    @Column(name = "last_notification")
    val lastNotification: LocalDateTime? = null,

    @Column(name = "senddate")
    val sendDate: LocalDateTime? = null,

    @Column(name = "docnum")
    val docNum: String? = null,

    @Column(name = "cargoname")
    val cargoName: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idclientfrom")
    val clientFrom: Client? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idclientto")
    val clientTo: Client? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idcityfrom")
    val cityFrom: City? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idcityto")
    val cityTo: City? = null,

    @Column(name = "packaging")
    val packaging: String? = null,

    @Column(name = "description")
    val description: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idprice")
    val price: Price? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idwarefrom")
    @org.hibernate.annotations.NotFound(action = org.hibernate.annotations.NotFoundAction.IGNORE)
    val wareFrom: Warehouse? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idwareto")
    @org.hibernate.annotations.NotFound(action = org.hibernate.annotations.NotFoundAction.IGNORE)
    val wareTo: Warehouse? = null
) 