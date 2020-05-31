package com.salesianostriana.sharecare.models

data class Solicitud(
    val descripcion: String,
    val fecha: String,
    val id: String,
    val nombreSolicitado: String,
    val nombreSolicitante: String,
    val phoneSolicitado: String,
    val phoneSolicitante: String
)