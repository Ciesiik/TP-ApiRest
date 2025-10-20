package com.example.tp_apirest.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoritoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun agregarFavorito(favorito: Favorito)

    @Query("DELETE FROM favorito_entity WHERE userId = :userId AND trackId = :trackId")
    fun eliminarFavorito(userId: Int, trackId: Long)

    @Query("SELECT trackId FROM favorito_entity WHERE userId = :userId")
    fun obtenerFavoritos(userId: Int): List<Long>

    @Query("SELECT EXISTS(SELECT 1 FROM favorito_entity WHERE userId = :userId AND trackId = :trackId)")
    fun esFavorito(userId: Int, trackId: Long): Boolean
}
