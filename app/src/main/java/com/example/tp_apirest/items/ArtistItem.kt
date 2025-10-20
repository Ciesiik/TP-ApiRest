package com.example.tp_apirest.items

data class ArtistItem(
    val id: Long,
    val nombre: String,
    val fotoUrl: String,
    val posicion: Int,
    val numeroAlbums: Int?,
    val numeroFans: Int?
)
