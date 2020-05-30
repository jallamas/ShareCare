package org.jallamas.dam.sharecare.users

import org.jallamas.dam.sharecare.entidades.User
import org.jallamas.dam.sharecare.upload.ImgurImageAttribute
import java.util.*

data class UserDTO(
        var username : String,
        var fullName: String,
        var phone : String,
        var localidad : String,
        var servicioCuidados : Boolean,
        var precioHora : Float,
        var img : ImgurImageAttribute?,
        val id: UUID? = null
)

fun User.toUserDTO() = UserDTO(username, fullName, phone, localidad, servicioCuidados, precioHora, img, id)

data class CreateUserDTO(
        var username: String,
        var fullname: String,
        val password: String,
        val password2: String,
        val phone : String,
        val localidad : String,
        val servicioCuidados : Boolean,
        val precioHora : Float,
        val img : ImgurImageAttribute?
)

data class EditUserDTO(
        var fullname: String,
        val phone : String,
        val localidad : String,
        val servicioCuidados : Boolean,
        val precioHora : Float
)