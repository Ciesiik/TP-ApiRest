package com.example.tp_apirest.dtos

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GenreDTO(
    val id: Long?,
    val name: String?
)

@JsonClass(generateAdapter = true)
data class GenreListDTO(
    val data: List<GenreDTO>
)
