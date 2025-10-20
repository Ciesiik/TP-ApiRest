package com.example.tp_apirest.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuario_entity")
    fun getAll(): List<Usuario>

    @Insert
    fun insert(usuario: Usuario)

    @Query("SELECT * FROM usuario_entity WHERE nombre = :nombre")
    fun buscarUsuario(nombre: String): Usuario?

    @Query("SELECT * FROM usuario_entity WHERE nombre = :nombre AND contraseña = :contraseña")
    fun login(nombre: String, contraseña: String): Usuario?
}