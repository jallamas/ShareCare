package com.salesianostriana.sharecare.models.responses

import com.salesianostriana.sharecare.models.User

data class LoginResponse(
    val token: String,
    val user: User
)