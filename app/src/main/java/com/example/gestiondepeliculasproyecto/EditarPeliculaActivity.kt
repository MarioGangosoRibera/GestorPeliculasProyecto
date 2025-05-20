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

class EditarPeliculaActivity : AppCompatActivity() {

    private lateinit var editTextTitulo: TextInputEditText
    private lateinit var editTextGenero: TextInputEditText
    private lateinit var editTextAnio: TextInputEditText
    private lateinit var botonGuardar: Button

    private val peliculaDao: PeliculaDao by lazy {
        BaseDeDatosPelicula.obtenerBaseDeDatos(application).peliculaDao()
    }

    private var peliculaId: Int = 0  // ID para identificar la película a editar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_pelicula)

        editTextTitulo = findViewById(R.id.edit_text_titulo)
        editTextGenero = findViewById(R.id.edit_text_genero)
        editTextAnio = findViewById(R.id.edit_text_anio)
        botonGuardar = findViewById(R.id.boton_guardar)

        // Cambiar texto del botón
        botonGuardar.text = "Actualizar"

        // Obtener datos que vienen en el Intent (id y campos)
        peliculaId = intent.getIntExtra("id", 0)
        val titulo = intent.getStringExtra("titulo") ?: ""
        val genero = intent.getStringExtra("genero") ?: ""
        val anio = intent.getIntExtra("anio", 0)

        // Rellenar campos con los datos
        editTextTitulo.setText(titulo)
        editTextGenero.setText(genero)
        editTextAnio.setText(anio.toString())

        botonGuardar.setOnClickListener {
            actualizarPelicula()
        }
    }

    private fun actualizarPelicula() {
        val titulo = editTextTitulo.text.toString().trim()
        val genero = editTextGenero.text.toString().trim()
        val anioStr = editTextAnio.text.toString().trim()

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

        val peliculaActualizada = Pelicula(
            id = peliculaId,
            titulo = titulo,
            genero = genero,
            anio = anio
        )

        CoroutineScope(Dispatchers.IO).launch {
            peliculaDao.actualizar(peliculaActualizada)

            runOnUiThread {
                Toast.makeText(this@EditarPeliculaActivity, "Película actualizada", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
