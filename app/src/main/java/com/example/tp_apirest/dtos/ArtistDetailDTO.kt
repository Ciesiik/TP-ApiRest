package com.example.tp_apirest.dtos

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ArtistDetailDTO(
    val id: Long?,
    val name: String?,
    val picture_xl: String?,
    val nb_album: Int?,
    val nb_fan: Int?,
    val tracklist: String?
)
