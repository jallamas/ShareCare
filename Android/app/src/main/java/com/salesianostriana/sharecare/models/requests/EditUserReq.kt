package com.salesianostriana.sharecare.models.requests

data class EditUserReq (
    val fullname: String,
    val phone: String,
    val localidad: String,
    val servicioCuidados: String,
    val precioHora: String
)