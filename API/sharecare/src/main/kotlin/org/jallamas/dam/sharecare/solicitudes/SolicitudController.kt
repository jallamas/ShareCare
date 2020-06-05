package org.jallamas.dam.sharecare.solicitudes


import org.jallamas.dam.sharecare.entidades.Solicitud
import org.jallamas.dam.sharecare.entidades.MyUser
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestController
@RequestMapping("/solicitud")
class SolicitudController (
        val solicitudService: SolicitudService,
        val solicitudRepository: SolicitudRepository
){
    private fun todasLasSolicitudes(): List<Solicitud> {
        var result: List<Solicitud>
        with(solicitudRepository) {
            result = findAll()
        }
        if (result.isEmpty())
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "No hay solicitudes")
        return result
    }

    private fun unaSolicitud(id: UUID): Solicitud {
        var result: Optional<Solicitud>
        with(solicitudRepository) {
            result = findById(id)
        }
        return result.orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado la solicitud con el identificador $id") }
    }

    @GetMapping("/emitidas/")
    fun todasEmitidasPorUsuario(@AuthenticationPrincipal myUser : MyUser): ResponseEntity<List<Solicitud>> {
        var result: List<Solicitud>

        result = todasLasSolicitudes().filter { it.solicitante.equals(myUser) }.map { it }

        if (result.isEmpty())
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "No hay solicitudes hechas por este usuario")
        return ResponseEntity.status(HttpStatus.OK).body(result)

    }

    @GetMapping("/recibidas/")
    fun todasRecibidasPorUsuario(@AuthenticationPrincipal myUser : MyUser) : ResponseEntity<List<Solicitud>> {
        var result: List<Solicitud>

        result = todasLasSolicitudes().filter { it.solicitado.equals(myUser) }.map { it }

        if (result.isEmpty())
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "No hay solicitudes recibidas por este usuario")
        return ResponseEntity.status(HttpStatus.OK).body(result)
    }

    @GetMapping("/{solicitudId}")
    fun detalleSolicitud(@PathVariable solicitudId : UUID) = unaSolicitud(solicitudId)

    @PostMapping("/{userId}")
    fun createSolicitud(@PathVariable userId : UUID, @AuthenticationPrincipal myUser : MyUser, @RequestBody createSolicitud: SolicitudDTO) : ResponseEntity<SolicitudDTO> =
            solicitudService.createSolicitud(createSolicitud,myUser,userId).map{ResponseEntity.status(HttpStatus.CREATED).body(createSolicitud)}.orElseThrow {
                ResponseStatusException(HttpStatus.BAD_REQUEST, "Se produjo un error")
            }

    @DeleteMapping("/{solicitudId}")
    fun deleteSolicitud(@PathVariable solicitudId : UUID) : ResponseEntity<Void> {
        solicitudRepository.deleteById(solicitudId)
        return ResponseEntity.noContent().build()
    }

    @PutMapping("/{solicitudId}")
    fun editSolicitud(@PathVariable solicitudId : UUID, @AuthenticationPrincipal myUser: MyUser, @RequestBody editSolicitud: SolicitudDTO) : ResponseEntity<SolicitudDTO> =
            solicitudService.edit(editSolicitud , solicitudId).map { ResponseEntity.status(HttpStatus.OK).body(editSolicitud) }.orElseThrow {
                ResponseStatusException(HttpStatus.BAD_REQUEST, "Se produjo un error")}

}