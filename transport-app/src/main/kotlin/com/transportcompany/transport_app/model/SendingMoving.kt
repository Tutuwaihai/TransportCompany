package com.transportcompany.transport_app.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "proton_sending_moving")
data class SendingMoving(
    @Id
    val id: Long,

    @Column(name = "modifydate")
    val modifyDate: LocalDateTime? = null,

    @Column(name = "createdate")
    val createDate: LocalDateTime = LocalDateTime.now(),

    @Column(name = "createuser")
    val createUser: Long? = null,

    @Column(name = "modifyuser")
    val modifyUser: Long? = null,

    @Column(name = "isdeleted")
    val isDeleted: Int = 0,

    @Column(name = "type_pointer")
    var typePointer: Int,

    @Column(name = "idpointer")
    var idPointer: Long? = null,

    @Column(name = "idcityfrom")
    val idCityFrom: Long? = null,

    @Column(name = "idcityto")
    val idCityTo: Long? = null,

    @Column(name = "num_count")
    val numCount: Int? = null,

    @Column(name = "num_places")
    val numPlaces: Int? = null,

    @Column(name = "id_brother")
    val idBrother: Long? = null,

    @Column(name = "idroute")
    val idRoute: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idsending", referencedColumnName = "id")
    val sending: Sending? = null
)