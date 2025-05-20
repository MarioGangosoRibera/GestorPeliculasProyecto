package com.example.gestiondepeliculasproyecto

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
        adaptador = AdaptadorPelicula()
        recyclerView.adapter = adaptador
        // Cargar las películas al iniciar
        cargarPeliculas()
        // Configurar el botón para agregar películas
        findViewById<Button>(R.id.boton_agregar_pelicula).setOnClickListener {
            // Lógica para agregar una nueva película
        }
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