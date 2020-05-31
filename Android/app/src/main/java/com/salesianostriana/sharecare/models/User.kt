package com.salesianostriana.sharecare.models

data class User(
    val fullName: String,
    val id: String,
    val img: String,
    val localidad: String,
    val phone: String,
    val precioHora: Float,
    val servicioCuidados: Boolean,
    val username: String
)