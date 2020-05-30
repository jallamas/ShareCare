package org.jallamas.dam.sharecare.entidades

import com.fasterxml.jackson.annotation.JsonBackReference
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*


@Entity
data class Solicitud (

        var descripcion : String,
        var fecha : LocalDateTime = LocalDateTime.now(),
        @JsonBackReference @ManyToOne var solicitante : User,
        @JsonBackReference @ManyToOne var solicitado : User,
        var nombreSolicitante: String = solicitante.fullName,
        var phoneSolicitante: String = solicitante.phone,
        var nombreSolicitado:String = solicitado.fullName,
        var phoneSolicitado: String = solicitado.phone,
        @Id @GeneratedValue val id : UUID? = null) {

}