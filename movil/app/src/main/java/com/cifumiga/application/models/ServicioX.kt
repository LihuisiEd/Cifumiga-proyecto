package com.cifumiga.application.models

data class ServicioX(
    val id: Int,
    val servicio_desc: String,
    val servicio_area: String,
    val servicio_dim: String,
    val servicio_frec: String,
    val servicio_precio: String,
    val tipo: String,
    val cliente: Int
)