package com.example.gestiondepeliculasproyecto.modelo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "peliculas")
data class Pelicula (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val genero: String,
    val anio: Int,
    val imagenUri: String? = null
)