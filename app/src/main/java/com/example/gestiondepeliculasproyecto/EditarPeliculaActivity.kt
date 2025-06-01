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

class EditarPeliculaActivity : AppCompatActivity() {

    private lateinit var editTextTitulo: TextInputEditText
    private lateinit var editTextGenero: TextInputEditText
    private lateinit var editTextAnio: TextInputEditText
    private lateinit var botonGuardar: Button
    private lateinit var imageViewSeleccionar: ImageView

    private val peliculaDao: PeliculaDao by lazy {
        BaseDeDatosPelicula.obtenerBaseDeDatos(application).peliculaDao()
    }

    private var peliculaId: Int = 0
    private var uriImagenSeleccionada: Uri? = null
    private val PICK_IMAGE_REQUEST = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_pelicula)

        editTextTitulo = findViewById(R.id.edit_text_titulo)
        editTextGenero = findViewById(R.id.edit_text_genero)
        editTextAnio = findViewById(R.id.edit_text_anio)
        botonGuardar = findViewById(R.id.boton_guardar)
        imageViewSeleccionar = findViewById(R.id.image_view_seleccionar)

        botonGuardar.text = "Actualizar"

        // Obtener datos desde el intent
        peliculaId = intent.getIntExtra("id", 0)
        val titulo = intent.getStringExtra("titulo") ?: ""
        val genero = intent.getStringExtra("genero") ?: ""
        val anio = intent.getIntExtra("anio", 0)
        val imagenUri = intent.getStringExtra("imagenUri")

        // Rellenar campos
        editTextTitulo.setText(titulo)
        editTextGenero.setText(genero)
        editTextAnio.setText(anio.toString())

        // Mostrar imagen si existe
        if (!imagenUri.isNullOrEmpty()) {
            uriImagenSeleccionada = Uri.parse(imagenUri)
            imageViewSeleccionar.setImageURI(uriImagenSeleccionada)
        } else {
            imageViewSeleccionar.setImageResource(R.drawable.image_search_24)
        }

        // Permitir seleccionar nueva imagen
        imageViewSeleccionar.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "image/*"
                addCategory(Intent.CATEGORY_OPENABLE)
            }
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        // Guardar cambios
        botonGuardar.setOnClickListener {
            actualizarPelicula()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                uriImagenSeleccionada = uri
                imageViewSeleccionar.setImageURI(uri)
            }
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
            anio = anio,
            imagenUri = uriImagenSeleccionada?.toString()
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
