package com.example.gestiondepeliculasproyecto.modelo

import androidx.room.Entity
import androidx.room.PrimaryKey

//Modelo de datos que representa la película en la base de datos local
@Entity(tableName = "peliculas") //Room generá una tabla peliculas
data class Pelicula (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,// Id de la película (se genera automáticamente)
    val titulo: String,
    val genero: String,
    val anio: Int,
    val imagenUri: String? = null
)