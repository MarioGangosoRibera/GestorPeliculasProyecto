package com.example.gestiondepeliculasproyecto.modelo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PeliculaDao {
    @Insert
    suspend fun insertar(pelicula: Pelicula)

    @Query("SELECT * FROM peliculas")
    suspend fun obtenerTodasLasPeliculas(): List<Pelicula>

    @Delete
    suspend fun eliminar(pelicula: Pelicula)

    @Update
    suspend fun actualizar(pelicula: Pelicula)
}