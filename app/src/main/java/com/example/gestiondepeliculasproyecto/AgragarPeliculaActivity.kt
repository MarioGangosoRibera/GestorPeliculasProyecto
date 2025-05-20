package com.example.gestiondepeliculasproyecto

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gestiondepeliculasproyecto.data.BaseDeDatosPelicula
import com.example.gestiondepeliculasproyecto.modelo.Pelicula
import com.example.gestiondepeliculasproyecto.modelo.PeliculaDao
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AgregarPeliculaActivity : AppCompatActivity() {

    private lateinit var editTextTitulo: TextInputEditText
    private lateinit var editTextGenero: TextInputEditText
    private lateinit var editTextAnio: TextInputEditText
    private lateinit var botonGuardar: Button

    private val peliculaDao: PeliculaDao by lazy {
        BaseDeDatosPelicula.obtenerBaseDeDatos(application).peliculaDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_pelicula)

        // Inicializar vistas
        editTextTitulo = findViewById(R.id.edit_text_titulo)
        editTextGenero = findViewById(R.id.edit_text_genero)
        editTextAnio = findViewById(R.id.edit_text_anio)
        botonGuardar = findViewById(R.id.boton_guardar)

        // Configurar el botón para guardar la película
        botonGuardar.setOnClickListener {
            guardarPelicula()
        }
    }

    private fun guardarPelicula() {
        val titulo = editTextTitulo.text.toString().trim()
        val genero = editTextGenero.text.toString().trim()
        val anioStr = editTextAnio.text.toString().trim()

        // Validar entradas
        if (titulo.isEmpty() || genero.isEmpty() || anioStr.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val anio = try {
            anioStr.toInt()
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "El año debe ser un número válido", Toast.LENGTH_SHORT).show()
            return
        }

        // Crear objeto Pelicula
        val nuevaPelicula = Pelicula(
            titulo = titulo,
            genero = genero,
            anio = anio
        )

        // Guardar en la base de datos
        CoroutineScope(Dispatchers.IO).launch {
            peliculaDao.insertar(nuevaPelicula)

            // Volver al hilo principal
            runOnUiThread {
                Toast.makeText(
                    this@AgregarPeliculaActivity,
                    "Película guardada con éxito",
                    Toast.LENGTH_SHORT
                ).show()

                // Cerrar esta Activity y volver a la principal
                finish()
            }
        }
    }
}