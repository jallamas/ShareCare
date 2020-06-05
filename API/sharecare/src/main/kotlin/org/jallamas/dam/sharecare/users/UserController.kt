package org.jallamas.dam.sharecare.users

import org.jallamas.dam.sharecare.entidades.MyUser
import org.jallamas.dam.sharecare.extended.ExtendedFunctions.Companion.unwrap
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
import java.util.*
import kotlin.collections.ArrayList

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

    @PostMapping("/nophoto/")
    fun nuevoUsuarioSinImagen(@RequestBody newUser: CreateUserDTO): ResponseEntity<UserDTO> {

        var prevresult = userService.createSinImagen(newUser)
        var result :UserDTO?=null

        if (prevresult.isPresent) {

                result=prevresult.get().toUserDTO(null)

        }

        return ResponseEntity.status(HttpStatus.OK).body(result)
    }


    @PreAuthorize("isAuthenticated()")
    @PutMapping("/")
    fun editarUsuario(@AuthenticationPrincipal myUser: MyUser, @RequestBody editUser: EditUserDTO): ResponseEntity<EditUserDTO> =
            userService.edit(editUser, myUser).map { ResponseEntity.status(HttpStatus.OK).body(editUser) }.orElseThrow {
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{userId}")
    fun detalleUser(@PathVariable userId : UUID) = unUsuario(userId)

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/servicio/buscar{localidad}")
    fun usuariosConServicioPorLocalidad(@RequestParam("localidad") localidad : String): ResponseEntity<List<UserDTO>>{
        val prevresult = userService.findByServicioCuidadosTrueAndLocalidad(localidad)
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



    private fun unUsuario(userId: UUID): ResponseEntity<UserDTO> {
        var myUser: MyUser

        with(userService) {
            myUser = findById(userId).unwrap() as MyUser
        }
        var result :UserDTO = myUser.toUserDTO(null)
        if (myUser.img != null) {
            var resource = imgurStorageService.loadAsResource(myUser.img?.id!!)
            resource.ifPresent { x -> result = myUser.toUserDTO(x.url.toString()) }
        } else {
            myUser.toUserDTO(null)
        }
        return ResponseEntity.status(HttpStatus.OK).body(result)
    }

}