package org.jallamas.dam.sharecare

import org.jallamas.dam.sharecare.entidades.Solicitud
import org.jallamas.dam.sharecare.entidades.MyUser
import org.jallamas.dam.sharecare.solicitudes.SolicitudRepository
import org.jallamas.dam.sharecare.upload.ImgurImageAttribute
import org.jallamas.dam.sharecare.users.UserRepository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.time.LocalDate
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
		val user = MyUser("usuario", encoder.encode("usuario"),"José Antonio Llamas","954545454", "Sevilla"
					,true,4.5f, ImgurImageAttribute("X2zbtAP","zj25RFOMF5JBKOH"))
		userRepository.save(user)

		val user1 = MyUser("jallamas1", encoder.encode("12345678"),"Mariano Romero López","654789321", "Sevilla"
				,false,0f, ImgurImageAttribute("bp9qFpf","cHcr3t7xGOGUNgo"))
		userRepository.save(user1)

		val user2 = MyUser("jallamas2", encoder.encode("12345678"),"Federico Jiménez","654123789", "Sevilla"
				,true,4.5f, ImgurImageAttribute("ZvOe1OV","HDmUdb29rlPfHpn"))
		userRepository.save(user2)

		val user3 = MyUser("jallamas3", encoder.encode("12345678"),"Carmen Gaona","321456987", "Dos Hermanas"
				,true,7.5f, ImgurImageAttribute("DkbRq2t","xWUFB53FPz6eYP9"))
		userRepository.save(user3)

		val user4 = MyUser("jallamas4", encoder.encode("12345678"),"Jesús Martín","789654123", "Huelva"
				,true,6.5f, ImgurImageAttribute("LCg61vP","HZadeolHsXOzfPz"))
		userRepository.save(user4)

		val solicitudesHechaUser = listOf(
				Solicitud("Prueba1", LocalDate.of(2020,5,15),user,user1),
				Solicitud("Prueba2", LocalDate.of(2020,4,21),user,user2),
				Solicitud("Prueba3", LocalDate.of(2020,5,27),user3,user)
		)
		solicitudRepository.saveAll(solicitudesHechaUser)
	}

}