package com.farmaciagaby.models

import com.google.firebase.Timestamp

data class Detalle(
    val descripcion: String,
    val fecha: Timestamp,
    val empleado: String,
    val proveedor: String,
    val productos: List<Any?>
)