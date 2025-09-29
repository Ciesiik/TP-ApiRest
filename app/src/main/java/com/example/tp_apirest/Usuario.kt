package com.example.tp_apirest

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuario_entity")
data class Usuario(
    @ColumnInfo(name = "nombre") val nombre: String,
    @ColumnInfo(name = "contraseña") val contraseña: String
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
