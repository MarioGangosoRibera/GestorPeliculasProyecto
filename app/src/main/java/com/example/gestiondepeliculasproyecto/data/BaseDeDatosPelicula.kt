package com.example.gestiondepeliculasproyecto.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gestiondepeliculasproyecto.modelo.Pelicula
import com.example.gestiondepeliculasproyecto.modelo.PeliculaDao

@Database(entities = [Pelicula::class], version = 2)
abstract class BaseDeDatosPelicula : RoomDatabase(){
    abstract fun peliculaDao(): PeliculaDao

    companion object{
        @Volatile
        private var INSTANCIA : BaseDeDatosPelicula ?= null

        fun obtenerBaseDeDatos(context: Context): BaseDeDatosPelicula {
            return INSTANCIA ?: synchronized(this) {
                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    BaseDeDatosPelicula::class.java,
                    "base_de_datos_pelicula"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCIA = instancia
                instancia
            }
        }
    }
}