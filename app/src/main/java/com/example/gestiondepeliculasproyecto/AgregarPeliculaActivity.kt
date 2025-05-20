package com.example.gestiondepeliculasproyecto

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
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
    private lateinit var imageViewSeleccionar: ImageView

    private var imagenUri: Uri? = null

    private val peliculaDao: PeliculaDao by lazy {
        BaseDeDatosPelicula.obtenerBaseDeDatos(application).peliculaDao()
    }

    companion object {
        private const val CODIGO_SELECCION_IMAGEN = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_pelicula)

        // Inicializar vistas
        editTextTitulo = findViewById(R.id.edit_text_titulo)
        editTextGenero = findViewById(R.id.edit_text_genero)
        editTextAnio = findViewById(R.id.edit_text_anio)
        botonGuardar = findViewById(R.id.boton_guardar)
        imageViewSeleccionar = findViewById(R.id.image_view_seleccionar)

        // Click para seleccionar imagen
        imageViewSeleccionar.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, CODIGO_SELECCION_IMAGEN)
        }

        // Click para guardar
        botonGuardar.setOnClickListener {
            guardarPelicula()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CODIGO_SELECCION_IMAGEN && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                imagenUri = uri
                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                imageViewSeleccionar.setImageURI(uri)
            }
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

        // Crear objeto Pelicula con imagenUri si existe
        val nuevaPelicula = Pelicula(
            titulo = titulo,
            genero = genero,
            anio = anio,
            imagenUri = imagenUri?.toString()
        )

        // Guardar en base de datos
        CoroutineScope(Dispatchers.IO).launch {
            peliculaDao.insertar(nuevaPelicula)

            runOnUiThread {
                Toast.makeText(
                    this@AgregarPeliculaActivity,
                    "Película guardada con éxito",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }
}
