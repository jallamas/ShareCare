package org.jallamas.dam.sharecare

import org.jallamas.dam.sharecare.entidades.Solicitud
import org.jallamas.dam.sharecare.entidades.User
import org.jallamas.dam.sharecare.solicitudes.SolicitudRepository
import org.jallamas.dam.sharecare.users.UserRepository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime
import javax.annotation.PostConstruct

@SpringBootApplication
class SharecareApplication

fun main(args: Array<String>) {
	runApplication<SharecareApplication>(*args)
}

@Component
class InitDataComponent(
		val solicitudRepository: SolicitudRepository,
		val userRepository: UserRepository,
		private val encoder: PasswordEncoder
) {

	@PostConstruct
	fun initData()  {
		val user = User("jallamas", encoder.encode("12345678"),"José Antonio Llamas","954545454", "Sevilla"
					,true,4.5f)
		userRepository.save(user)

		val user1 = User("jallamas1", encoder.encode("12345678"),"Mariano Romero López","954545454", "Sevilla"
				,false,0f)
		userRepository.save(user1)

		val user2 = User("jallamas2", encoder.encode("12345678"),"Federico Jiménez","954545454", "Sevilla"
				,true,4.5f)
		userRepository.save(user2)

		val user3 = User("jallamas3", encoder.encode("12345678"),"José Antonio Llamas","954545454", "Sevilla"
				,true,7.5f)
		userRepository.save(user3)

		val user4 = User("jallamas4", encoder.encode("12345678"),"José Antonio Llamas","954545454", "Sevilla"
				,true,6.5f)
		userRepository.save(user4)

		val solicitudesHechaUser = listOf(
				Solicitud("Prueba1", LocalDateTime.of(2020,5,15,13,45),user,user1),
				Solicitud("Prueba2", LocalDateTime.of(2020,4,21,17,32),user,user2)
		)
		solicitudRepository.saveAll(solicitudesHechaUser)
	}

}