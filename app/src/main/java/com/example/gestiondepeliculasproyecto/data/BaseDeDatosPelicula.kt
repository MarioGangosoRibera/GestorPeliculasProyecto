package com.example.gestiondepeliculasproyecto.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gestiondepeliculasproyecto.modelo.Pelicula
import com.example.gestiondepeliculasproyecto.modelo.PeliculaDao

//Clase que define la base de datos Room para las películas
@Database(entities = [Pelicula::class], version = 2)
abstract class BaseDeDatosPelicula : RoomDatabase(){

    //Devuelve el dao para acceder a la tabla peliculas
    abstract fun peliculaDao(): PeliculaDao

    companion object{
        //instancia de la base de datos
        @Volatile
        private var INSTANCIA : BaseDeDatosPelicula ?= null

        //Función para obtener la instancia de la base de datos
        fun obtenerBaseDeDatos(context: Context): BaseDeDatosPelicula {
            //Devuelve la instancia existente o crea una nueva
            return INSTANCIA ?: synchronized(this) {
                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    BaseDeDatosPelicula::class.java,
                    "base_de_datos_pelicula"
                )
                    //Borra y recrea la base de datos si hay un cambio de version
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCIA = instancia
                instancia
            }
        }
    }
}