package com.cifumiga.application.models

data class Kilometraje(
    val id: Int,
    val empleado: Int,
    val fecha: String,
    val placa: String,
    val kilometraje_fin: String,
    val kilometraje_inicio: String,
    val kilometraje_total: String
)