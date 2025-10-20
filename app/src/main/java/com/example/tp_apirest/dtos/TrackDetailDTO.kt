package com.example.tp_apirest.dtos

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TrackDetailDTO(
    val id: Long?,
    val title: String?,
    val duration: Int?,
    val preview: String?,
    val artist: ArtistDetailDTO?,
    val album: AlbumDetailDTO?,
    val release_date: String?,
    val rank: Int?,
    val track_position: Int?,
    val gain: Float?
)
