package org.jallamas.dam.sharecare.users

import org.jallamas.dam.sharecare.entidades.Solicitud
import org.jallamas.dam.sharecare.entidades.User
import java.util.*

data class UserDTO(
        var username : String,
        var fullName: String,
        var phone : String,
        var localidad : String,
        var servicioCuidados : Boolean,
        var precioHora : Float,
        val id: UUID? = null
)

fun User.toUserDTO() = UserDTO(username, fullName, phone, localidad, servicioCuidados, precioHora, id)

data class CreateUserDTO(
        var username: String,
        var fullname: String,
        val password: String,
        val password2: String,
        val phone : String,
        val localidad : String,
        val servicioCuidados : Boolean,
        val precioHora : Float
)

data class EditUserDTO(
        var fullname: String,
        val phone : String,
        val localidad : String,
        val servicioCuidados : Boolean,
        val precioHora : Float
)