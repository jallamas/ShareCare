package org.jallamas.dam.sharecare.users


import org.jallamas.dam.sharecare.entidades.MyUser
import org.jallamas.dam.sharecare.upload.ImgurImageAttribute
import org.jallamas.dam.sharecare.upload.ImgurStorageService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class UserService (
    val repo: UserRepository,
    val encoder: PasswordEncoder,
    val imgurStorageService: ImgurStorageService
    ) {

    fun create(newUser: CreateUserDTO, file: MultipartFile): Optional<MyUser> {

        var imageAttribute: ImgurImageAttribute? = null

        if (!file.isEmpty) {
            imageAttribute = imgurStorageService.store(file).orElse(null)

        }

        if (findByUsername(newUser.username).isPresent){
            return Optional.empty()
        }else {
            return Optional.of(
                    with(newUser) {
                        repo.save(MyUser(username, encoder.encode(password), fullname, phone, localidad, servicioCuidados,
                                precioHora,imageAttribute, mutableSetOf("USER")))
                    }
            )
        }
    }

    fun createSinImagen(newUser: CreateUserDTO): Optional<MyUser> {

        var imageAttribute: ImgurImageAttribute? = null

        if (findByUsername(newUser.username).isPresent){
            return Optional.empty()
        }else {
            return Optional.of(
                    with(newUser) {
                        repo.save(
                                MyUser(
                                        username,
                                        encoder.encode(password),
                                        fullname,
                                        phone,
                                        localidad,
                                        servicioCuidados,
                                        precioHora,
                                        imageAttribute,
                                        mutableSetOf("USER")
                                )
                        )
                    }
            )
        }
    }

    fun edit(editedUser: EditUserDTO, myUser:MyUser) : Optional<MyUser>{
        with(myUser) {
            fullName = editedUser.fullname
            localidad = editedUser.localidad
            phone = editedUser.phone
            servicioCuidados = editedUser.servicioCuidados
            precioHora = editedUser.precioHora
        }
        repo.save(myUser)

        return Optional.of(myUser)
    }

    fun findByUsername(username: String) = repo.findByUsername(username)

    fun findById(id: UUID) = repo.findById(id)

    fun findByServicioCuidadosTrue() = repo.findByServicioCuidadosTrue()

    fun findByServicioCuidadosTrueAndLocalidad(localidad: String) =
            repo.findByServicioCuidadosTrueAndLocalidadContainingIgnoreCase(localidad)

}