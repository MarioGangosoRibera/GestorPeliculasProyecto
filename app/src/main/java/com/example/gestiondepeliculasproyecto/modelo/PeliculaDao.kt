package com.example.gestiondepeliculasproyecto.modelo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

//interfaz dao que define las operaciones sobre la tabla peliculas
@Dao
interface PeliculaDao {
    //Inserta una nueva pelicula en la base de datos
    @Insert
    suspend fun insertar(pelicula: Pelicula)

    //Obtiene todas las peliculas
    @Query("SELECT * FROM peliculas")
    suspend fun obtenerTodasLasPeliculas(): List<Pelicula>

    //Borra una pelicula
    @Delete
    suspend fun eliminar(pelicula: Pelicula)

    //Actualiza una pelicula
    @Update
    suspend fun actualizar(pelicula: Pelicula)
}