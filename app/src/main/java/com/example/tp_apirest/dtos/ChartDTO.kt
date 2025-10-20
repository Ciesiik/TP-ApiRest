package com.example.tp_apirest.dtos

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChartDTO(
    val tracks: TrackListDTO,
    val albums: AlbumListDTO,
    val artists: ArtistListDTO
)

@JsonClass(generateAdapter = true)
data class TrackListDTO(
    val data: List<TrackDTO>
)
@JsonClass(generateAdapter = true)
data class AlbumListDTO(
    val data: List<AlbumDTO>
)
@JsonClass(generateAdapter = true)
data class ArtistListDTO(
    val data: List<ArtistDTO>
)

@JsonClass(generateAdapter = true)
data class TrackDTO(
    val id: Long?,
    val title: String?,
    val duration: Int?,
    val artist: ArtistDTO?,
    val album: AlbumDTO?
)

@JsonClass(generateAdapter = true)
data class ArtistDTO(
    val id: Long?,
    val name: String?,
    val picture_xl: String?,
    val position: Int?
)

@JsonClass(generateAdapter = true)
data class AlbumDTO(
    val id: Long?,
    val title: String?,
    val cover_xl: String?,
    val record_type: String?,
    val position: Int?,
    val artist: ArtistDTO?
)

