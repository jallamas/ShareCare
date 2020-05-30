package org.jallamas.dam.sharecare.entidades

import com.fasterxml.jackson.annotation.JsonManagedReference
import org.jallamas.dam.sharecare.upload.ImgurImageAttribute
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.*
import javax.persistence.*
import kotlin.collections.HashSet
import org.springframework.security.core.userdetails.UserDetails

@Entity
data class User (
        @Column(nullable = false, unique = true)
        private var username: String,
        private var password: String,
        var fullName : String,
        var phone : String,
        var localidad : String,
        var servicioCuidados : Boolean,
        var precioHora : Float,
        var img : ImgurImageAttribute? = null,

        @ElementCollection(fetch = FetchType.EAGER)
        val roles: MutableSet<String> = HashSet(),

        private val nonExpired: Boolean = true,
        private val nonLocked: Boolean = true,
        private val enabled: Boolean = true,
        private val credentialsNonExpired : Boolean = true,

        @JsonManagedReference @OneToMany(mappedBy="solicitante", fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
        var solicitudesHechas : List<Solicitud>? = null,
        @JsonManagedReference @OneToMany(mappedBy="solicitado", fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
        var solicitudesRecibidas : List<Solicitud>? = null,



        @Id @GeneratedValue val id : UUID? = null
) : UserDetails{
        constructor(username: String, password: String, fullName: String, phone: String, localidad: String, servicioCuidados: Boolean, precioHora: Float, img : ImgurImageAttribute?) :
                this(username, password, fullName, phone, localidad, servicioCuidados, precioHora, img,
                        mutableSetOf("USER"), true, true, true, true)

        override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
                roles.map { SimpleGrantedAuthority("ROLE_$it") }.toMutableList()

        override fun isEnabled() = enabled
        override fun getUsername() = username
        override fun isCredentialsNonExpired() = credentialsNonExpired
        override fun getPassword() = password
        override fun isAccountNonExpired() = nonExpired
        override fun isAccountNonLocked() = nonLocked

        override fun equals(other: Any?): Boolean {
                if (this === other)
                        return true
                if (other === null || other !is User)
                        return false
                if (this::class != other::class)
                        return false
                return id == other.id
        }

        override fun hashCode(): Int {
                if (id == null)
                        return super.hashCode()
                return id.hashCode()
        }
}