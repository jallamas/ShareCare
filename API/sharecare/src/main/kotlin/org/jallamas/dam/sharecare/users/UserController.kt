package org.jallamas.dam.sharecare.users

import org.jallamas.dam.sharecare.entidades.User
import org.jallamas.dam.sharecare.upload.ImgurBadRequest
import org.jallamas.dam.sharecare.upload.ImgurStorageService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException

@Controller
@RequestMapping("/user")
class UserController (
        val userService: UserService,
        val imgurStorageService: ImgurStorageService
) {

    @PostMapping("/")
    fun nuevoUsuario(@RequestPart("new") newUser: CreateUserDTO,
                     @RequestPart("file") file: MultipartFile): ResponseEntity<UserDTO> {

        var prevresult = userService.create(newUser, file)
        var result :UserDTO?=null

        if (prevresult.isPresent) {
            if (prevresult.get().img != null) {
                var resource = imgurStorageService.loadAsResource(prevresult.get().img?.id!!)
                resource.ifPresent { x -> result = prevresult.get().toUserDTO(x.url.toString()) }
            } else {
                result=prevresult.get().toUserDTO(null)
            }
        }
        try{
        return ResponseEntity.status(HttpStatus.OK).body(result)

        } catch (ex: ImgurBadRequest) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Se produjo un error al subir la imagen")
        }
    }


    @PreAuthorize("isAuthenticated()")
    @PutMapping("/")
    fun editarUsuario(@AuthenticationPrincipal user: User, @RequestBody editUser: EditUserDTO): ResponseEntity<EditUserDTO> =
            userService.edit(editUser, user).map { ResponseEntity.status(HttpStatus.OK).body(editUser) }.orElseThrow {
                ResponseStatusException(HttpStatus.BAD_REQUEST, "Se produjo un error")
            }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/servicio/")
    fun usuariosConServicio(): ResponseEntity<List<UserDTO>> {
        var prevresult = userService.findByServicioCuidadosTrue()
        var result = ArrayList<UserDTO>()
        if (prevresult.isNotEmpty()) {

            prevresult.forEach {

                if (it.img != null) {
                    var resource = imgurStorageService.loadAsResource(it.img?.id!!)
                    resource.ifPresent { x -> result.add(it.toUserDTO(x.url.toString())) }
                } else {
                    result.add(it.toUserDTO(null))
                }
            }

            if (result.isEmpty())
                throw ResponseStatusException(HttpStatus.NOT_FOUND, "No hay usuarios que ofrezcan el servicio en este momento")

        }
        return ResponseEntity.status(HttpStatus.OK).body(result)
    }
}