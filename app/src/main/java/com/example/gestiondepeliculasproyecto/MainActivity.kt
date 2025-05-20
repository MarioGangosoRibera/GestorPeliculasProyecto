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
    private lateinit var recyclerView: RecyclerView
    private lateinit var adaptador: AdaptadorPelicula
    private val peliculaDao: PeliculaDao by lazy {
        BaseDeDatosPelicula.obtenerBaseDeDatos(application).peliculaDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adaptador = AdaptadorPelicula{ peliculaAEliminar ->
           CoroutineScope(Dispatchers.IO).launch {
               peliculaDao.eliminar(peliculaAEliminar)
               val peliculasActualizadas = peliculaDao.obtenerTodasLasPeliculas()
               runOnUiThread{
                   adaptador.setPeliculas(peliculasActualizadas)
                   Toast.makeText(this@MainActivity, "Pelicula eliminada", Toast.LENGTH_SHORT).show()
               }
           }
        }
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

    private fun cargarPeliculas() {
        CoroutineScope(Dispatchers.IO).launch {
            val peliculas = peliculaDao.obtenerTodasLasPeliculas()
            runOnUiThread {
                adaptador.setPeliculas(peliculas)
            }
        }
    }
}