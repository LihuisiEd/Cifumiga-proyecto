package com.cifumiga.application.models

data class Tramite(
    val id: Int,
    val cliente: String,
    val tipo: String,
    val direccion: String,
    val referencia: String,
    val tramite_contacto: String,
    val tramite_telefono: String,
    val tramite_fecha: String,
    val tramite_nivel_1: Boolean,
    val tramite_nivel_2: Boolean,
    val tramite_nivel_3: Boolean,
    val tramite_nivel_4: Boolean,
    val problemas: String,
    val condicion_subestandar: String
)