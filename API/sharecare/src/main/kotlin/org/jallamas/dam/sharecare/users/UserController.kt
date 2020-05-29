package org.jallamas.dam.sharecare.users

import org.jallamas.dam.sharecare.entidades.User
import org.jallamas.dam.sharecare.users.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@Controller
@RequestMapping("/user")
class UserController (val userService: UserService) {

    @PostMapping("/")
    fun nuevoUsuario(@RequestBody newUser : CreateUserDTO): ResponseEntity<UserDTO> =
            userService.create(newUser).map { ResponseEntity.status(HttpStatus.CREATED).body(it.toUserDTO()) }.orElseThrow {
                ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de usuario ${newUser.username} ya existe")
            }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/")
    fun editarUsuario(@AuthenticationPrincipal user: User, @RequestBody editUser: EditUserDTO) : ResponseEntity<EditUserDTO> =
    userService.edit(editUser, user).map { ResponseEntity.status(HttpStatus.OK).body(editUser) }.orElseThrow {
        ResponseStatusException(HttpStatus.BAD_REQUEST, "Se produjo un error")}

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/servicio/")
    fun usuariosConServicio(): ResponseEntity<List<UserDTO>> {
        var result: List<UserDTO>

        result = userService.findByServicioCuidadosTrue()

        if (result.isEmpty())
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "No hay usuarios que ofrezcan el servicio en este momento")
        return ResponseEntity.status(HttpStatus.OK).body(result)
    }

}