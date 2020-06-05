package org.jallamas.dam.sharecare.solicitudes

import org.jallamas.dam.sharecare.entidades.Solicitud
import org.jallamas.dam.sharecare.entidades.MyUser
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SolicitudRepository : JpaRepository<Solicitud, UUID> {

    fun findById (solicitudId:String) : Solicitud

    fun findBySolicitado (myUser: MyUser) : List<Solicitud>

    fun findBySolicitante (myUser: MyUser) : List<Solicitud>
}