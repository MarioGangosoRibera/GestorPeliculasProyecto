package com.example.gestiondepeliculasproyecto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestiondepeliculasproyecto.data.BaseDeDatosPelicula
import com.example.gestiondepeliculasproyecto.modelo.PeliculaDao
import com.example.gestiondepeliculasproyecto.ui.AdaptadorPelicula
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    //Referencia al RecyclerView donde se listarán las películas
    private lateinit var recyclerView: RecyclerView
    //Adaptador personalizado que se conecta con el RecyclerView
    private lateinit var adaptador: AdaptadorPelicula
    //Acceso al DAO de películas
    private val peliculaDao: PeliculaDao by lazy {
        BaseDeDatosPelicula.obtenerBaseDeDatos(application).peliculaDao()
    }

    //Método de inicialización de la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Layout asociado a esta actividad

        //Inicializar el RecyclerView y establecer su diseño en forma de lista vertical
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        //Crear instancia del adaptador, pasando una lambda que se ejecuta al borrar una película
        adaptador = AdaptadorPelicula{ peliculaAEliminar ->
            //Lanzamos una corrutina para eliminar la película de forma asíncrona
           CoroutineScope(Dispatchers.IO).launch {
               peliculaDao.eliminar(peliculaAEliminar) //Eliminar de la base de datos
               //Obtener la lista actualizada después de eliminar
               val peliculasActualizadas = peliculaDao.obtenerTodasLasPeliculas()

               //Actualizar la interfaz en el hilo principal
               runOnUiThread{
                   adaptador.setPeliculas(peliculasActualizadas)
                   Toast.makeText(this@MainActivity, "Pelicula eliminada", Toast.LENGTH_SHORT).show()
               }
           }
        }

        //Asignar el adaptador al RecyclerView
        recyclerView.adapter = adaptador

        // Configurar el botón para agregar películas
        findViewById<Button>(R.id.boton_agregar_pelicula).setOnClickListener {
            val intent = Intent(this, AgregarPeliculaActivity::class.java)
            startActivity(intent)
        }
    }

    // Se ejecuta cada vez que la actividad vuelve a ser visible
    override fun onResume() {
        super.onResume()
        // Recargar las películas cada vez que volvemos a la actividad
        cargarPeliculas()
    }

    //Función que carga todas las películas desde la base de datos y actualiza el adaptador
    private fun cargarPeliculas() {
        CoroutineScope(Dispatchers.IO).launch {
            val peliculas = peliculaDao.obtenerTodasLasPeliculas() //Obtener desde Room

            //Actualizar UI en el hilo principal
            runOnUiThread {
                adaptador.setPeliculas(peliculas)
            }
        }
    }
}