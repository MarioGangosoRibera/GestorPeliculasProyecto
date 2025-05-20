package com.example.gestiondepeliculasproyecto.modelo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PeliculaDao {
    @Insert
    suspend fun insertar(pelicula: Pelicula)

    @Query("SELECT * FROM peliculas")
    suspend fun obtenerTodasLasPeliculas(): List<Pelicula>
}