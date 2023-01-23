package com.cifumiga.application.models

data class Tramite(
    val id:String?= "",
    val cliente: String?="",
    val tipo: String?="",
    val direccion: String?="",
    val referencia: String?="",
    val contacto: String?="",
    val telefono: String?="",
    val fecha: String?="",
    val culminacion: String?= "",
    val frecuencia: String?= "",
    val nivel_1: String?="",
    val nivel_2: String?="",
    val nivel_3: String?="",
    val nivel_4: String?="",
    val problemas: String?="",
    val condicion: String?="",
)