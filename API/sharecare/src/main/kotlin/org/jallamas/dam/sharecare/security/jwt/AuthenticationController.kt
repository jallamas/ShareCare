package org.jallamas.dam.sharecare.security.jwt

import org.jallamas.dam.sharecare.entidades.User
import org.jallamas.dam.sharecare.upload.ImgurStorageService
import org.jallamas.dam.sharecare.users.UserDTO
import org.jallamas.dam.sharecare.users.toUserDTO
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@RestController
class AuthenticationController(
        private val authenticationManager: AuthenticationManager,
        private val jwtTokenProvider: JwtTokenProvider,
        val imgurStorageService: ImgurStorageService
) {

    @PostMapping("/auth/login")
    fun login(@Valid @RequestBody loginRequest: LoginRequest): JwtUserResponse {
        val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                        loginRequest.username, loginRequest.password
                )
        )

        SecurityContextHolder.getContext().authentication = authentication

        val user = authentication.principal as User
        val jwtToken = jwtTokenProvider.generateToken(authentication)
        var result :UserDTO = user.toUserDTO(null)
        if (user.img != null) {
            var resource = imgurStorageService.loadAsResource(user.img?.id!!)
            resource.ifPresent { x -> result = user.toUserDTO(x.url.toString()) }
        } else {
            user.toUserDTO(null)
        }

        return JwtUserResponse(jwtToken, result)
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user/me")
    fun me(@AuthenticationPrincipal user: User) : UserDTO{
        var result :UserDTO = user.toUserDTO(null)
        if (user.img != null) {
            var resource = imgurStorageService.loadAsResource(user.img?.id!!)
            resource.ifPresent { x -> result = user.toUserDTO(x.url.toString()) }
        } else {
            user.toUserDTO(null)
        }
        return result
    }

    data class LoginRequest(
            @NotBlank val username: String,
            @NotBlank val password: String
    )

    data class JwtUserResponse(
            val token: String,
            val user: UserDTO
    )
}