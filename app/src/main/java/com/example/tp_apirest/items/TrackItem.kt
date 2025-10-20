package com.example.tp_apirest.items

data class TrackItem(
    var id: Long,
    var titulo: String,
    var autor: String,
    var fecha: String,
    var duracion: String,
    val imagenUrl: String
)