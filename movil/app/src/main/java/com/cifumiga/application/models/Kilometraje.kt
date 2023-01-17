package com.cifumiga.application.models

import com.google.firebase.Timestamp
import java.time.Instant

data class Kilometraje(
    val placa: String? = "",
    val inicial: String? = "0",
    val final: String? = "0",
    val total: String? = "0",
    val fecha: String? = "",
    val empleado: String? = ""
)