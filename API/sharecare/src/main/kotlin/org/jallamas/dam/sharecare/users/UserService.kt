package org.jallamas.dam.sharecare.users

import org.jallamas.dam.sharecare.entidades.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService (
    val repo: UserRepository,
    val encoder: PasswordEncoder
    ) {

    fun create(newUser: CreateUserDTO): Optional<User> {
        if (findByUsername(newUser.username).isPresent)
            return Optional.empty()
        return Optional.of(
                with(newUser) {
                    repo.save(User(username, encoder.encode(password), fullname, phone, localidad, servicioCuidados, precioHora,  mutableSetOf("USER")))
                }

        )
    }

    fun edit(editedUser: EditUserDTO, user:User) : Optional<User>{
        with(user) {
            fullName = editedUser.fullname
            localidad = editedUser.localidad
            phone = editedUser.phone
            servicioCuidados = editedUser.servicioCuidados
            precioHora = editedUser.precioHora
        }
        repo.save(user)

        return Optional.of(user)
    }

    fun findByUsername(username: String) = repo.findByUsername(username)

    fun findById(id: UUID) = repo.findById(id)

    fun findByServicioCuidadosTrue() = repo.findByServicioCuidadosTrue()

}