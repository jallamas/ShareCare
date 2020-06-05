package org.jallamas.dam.sharecare.solicitudes

import org.jallamas.dam.sharecare.entidades.Solicitud
import org.jallamas.dam.sharecare.entidades.MyUser
import org.jallamas.dam.sharecare.extended.ExtendedFunctions.Companion.unwrap
import org.jallamas.dam.sharecare.users.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.*

@Service
class SolicitudService (
        private val solicitudRepository: SolicitudRepository,
        private val userRepository: UserRepository
) {
//    fun <T> Optional<T>.unwrap(): T? = orElse(null)

    fun createSolicitud(solicitudDTO: SolicitudDTO, solicitante: MyUser, solicitado: UUID):Optional<Solicitud>{
        val myUserSolicitado : MyUser = userRepository.findById(solicitado).unwrap() as MyUser

        return Optional.of(
            with(solicitudDTO){
                solicitudRepository.save(Solicitud(descripcion, LocalDate.now(),solicitante,myUserSolicitado))
            }
        )
    }

    fun edit(editedSolicitud : SolicitudDTO, solicitudId : UUID) : Optional<Solicitud>{
        var solicitud : Solicitud = solicitudRepository.findById(solicitudId).unwrap() as Solicitud

        with(solicitud) {
            descripcion = editedSolicitud.descripcion
        }
        solicitudRepository.save(solicitud)

        return Optional.of(solicitud)
    }

}