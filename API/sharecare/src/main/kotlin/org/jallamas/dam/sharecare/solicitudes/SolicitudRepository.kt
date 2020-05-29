package org.jallamas.dam.sharecare.solicitudes

import org.jallamas.dam.sharecare.entidades.Solicitud
import org.jallamas.dam.sharecare.entidades.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SolicitudRepository : JpaRepository<Solicitud, UUID> {

    fun findById (solicitudId:String) : Solicitud

    fun findBySolicitado (user: User) : List<Solicitud>

    fun findBySolicitante (user: User) : List<Solicitud>
}