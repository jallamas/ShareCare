package org.jallamas.dam.sharecare.users

import org.jallamas.dam.sharecare.entidades.MyUser
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<MyUser, UUID> {

    fun findByUsername(username : String) : Optional<MyUser>

    fun findByServicioCuidadosTrue (): List<MyUser>

    fun findByServicioCuidadosTrueAndLocalidadContainingIgnoreCase(localidad : String) : List<MyUser>

}