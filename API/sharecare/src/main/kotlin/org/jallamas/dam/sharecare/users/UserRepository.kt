package org.jallamas.dam.sharecare.users

import org.jallamas.dam.sharecare.entidades.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User, UUID> {

    fun findByUsername(username : String) : Optional<User>

    fun findByServicioCuidadosTrue (): List<User>

    fun findByServicioCuidadosTrueAndLocalidadContainingIgnoreCase(localidad : String) : List<User>

}