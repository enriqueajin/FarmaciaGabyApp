package com.farmaciagaby.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import java.util.*
import kotlin.collections.HashMap

data class Employee(

    @PropertyName("direccion")
    val direccion: HashMap<String, String> = hashMapOf(),

    @PropertyName("email")
    var email: String = "",

    @PropertyName("fecha_contratacion")
    val fecha_contratacion: Timestamp = Timestamp(Date()),

    @PropertyName("numero_celular")
    val numero_celular: String = "",

    @PropertyName("primer_nombre")
    val primer_nombre: String = "",

    @PropertyName("segundo_nombre")
    val segundo_nombre: String = "",

    @PropertyName("primer_apellido")
    val primer_apellido: String = "",

    @PropertyName("segundo_apellido")
    val segundo_apellido: String = "",

    @PropertyName("telefono_casa")
    val telefono_casa: String = "",

    @PropertyName("uid")
    val uid: String = ""
)