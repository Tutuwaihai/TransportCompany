package com.transportcompany.transport_app.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "attached_documents")
data class AttachedDocuments(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(name = "modifydate")
    val modifyDate: LocalDateTime? = null,

    @Column(name = "createdate")
    val createDate: LocalDateTime = LocalDateTime.now(),

    @Column(name = "createuser")
    val createUser: Long? = null,

    @Column(name = "modifyuser")
    val modifyUser: Long? = null,

    @Column(name = "isdeleted")
    val isDeleted: Int? = null,

    @Column(name = "id_from_table")
    val fromTableId: Long? = null,

    @Column(name = "table_name")
    val tableName: String? = null,

    @Column(name = "url")
    val url: String? = null,

    @Column(name = "is_file")
    val isFile: String? = null,

    @Column(name = "document_type")
    val documentType: Int? = null,

    @Column(name = "id_ftp_server")
    val ftpServerId: Long? = null,
)
