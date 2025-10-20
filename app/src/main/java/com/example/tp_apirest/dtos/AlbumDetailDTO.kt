package com.example.tp_apirest.dtos

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AlbumDetailDTO(
    val id: Long?,
    val title: String?,
    val cover_xl: String?,
    val genres: GenreListDTO?,
    val label: String?,
    val nb_tracks: Int?,
    val duration: Int?,
    val fans: Int?,
    val release_date: String?,
    val record_type: String?,
    val tracklist: String?,
    val artist: ArtistDTO?
)
