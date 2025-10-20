package com.example.tp_apirest.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorito_entity",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["userId"]), Index(value = ["trackId"])]
)
data class Favorito(
    @ColumnInfo(name = "userId") val userId: Int,
    @ColumnInfo(name = "trackId") val trackId: Long
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
