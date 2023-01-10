package com.cifumiga.application.models

data class Cliente(
    val id: Int,
    val cliente_nombre: String,
    val cliente_ruc: String,
    val cliente_contacto: String,
    val cliente_telefono: String,
    val cliente_correo: String
)