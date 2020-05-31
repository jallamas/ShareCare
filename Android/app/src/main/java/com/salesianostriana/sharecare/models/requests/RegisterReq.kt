package com.salesianostriana.sharecare.models.requests

data class RegisterReq(
    val username: String,
    val fullname: String,
    val localidad: String,
    val password: String,
    val password2: String,
    val phone: String,
    val precioHora: String,
    val servicioCuidados: String
)