package com.example.tp_apirest.endpoints

import com.example.tp_apirest.dtos.AlbumDetailDTO
import com.example.tp_apirest.dtos.AlbumListDTO
import com.example.tp_apirest.dtos.ArtistDetailDTO
import com.example.tp_apirest.dtos.ArtistListDTO
import com.example.tp_apirest.dtos.ChartDTO
import com.example.tp_apirest.dtos.TrackDetailDTO
import com.example.tp_apirest.dtos.TrackListDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface DeezerApi {
    @GET("chart")
    fun getChart(): Call<ChartDTO>

    @GET ("track/{id}")
    fun getTrack(@Path("id") id: Long): Call<TrackDetailDTO>

    @GET("artist/{id}")
    fun getArtist(@Path("id") id: Long): Call<ArtistDetailDTO>

    @GET("album/{id}")
    fun getAlbum(@Path("id") id: Long): Call<AlbumDetailDTO>

    @GET
    fun getAlbumTracklist(@Url url: String): Call<TrackListDTO>

    @GET("artist/{id}/albums")
    fun getArtistAlbums(@Path("id") id: Long): Call<AlbumListDTO>

    @GET
    fun getArtistTopTracks(@Url tracklist: String): Call<TrackListDTO>

    @GET("search")
    fun searchTrack(
        @Query("q") query: String,
        @Query("limit") limit: Int = 30
    ): Call<TrackListDTO>


    @GET("search/album")
    fun searchAlbum(
        @Query("q") query: String,
        @Query("limit") limit: Int = 15
    ): Call<AlbumListDTO>


    @GET("search/artist")
    fun searchArtist(
        @Query("q") query: String,
        @Query("limit") limit: Int = 5
    ): Call<ArtistListDTO>





}