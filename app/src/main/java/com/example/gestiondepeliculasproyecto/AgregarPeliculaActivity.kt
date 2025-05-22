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

//Actividad que agrega una pelicula a la base de datos
class AgregarPeliculaActivity : AppCompatActivity() {

    //Variable para los campos del formulario
    private lateinit var editTextTitulo: TextInputEditText
    private lateinit var editTextGenero: TextInputEditText
    private lateinit var editTextAnio: TextInputEditText
    private lateinit var botonGuardar: Button
    private lateinit var imageViewSeleccionar: ImageView

    //Uri de la imagen seleccionada
    private var imagenUri: Uri? = null

    //Acceso al dao de la base de datos usando lazy para inicializacion
    private val peliculaDao: PeliculaDao by lazy {
        BaseDeDatosPelicula.obtenerBaseDeDatos(application).peliculaDao()
    }

    //Identificar la actividad de selección de imagen
    companion object {
        private const val CODIGO_SELECCION_IMAGEN = 100
    }

    //Método que se ejecuta al iniciar la actividad
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

    //Método que se ejecuta cuando se vuelve de una actividad (como el selector de imagen)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Verifica que el resultado proviene del selector de imagen y fue exitoso
        if (requestCode == CODIGO_SELECCION_IMAGEN && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                imagenUri = uri // Guarda el URI de la imagen seleccionada
                //Solicita permiso persistente para leer la imagen en el futuro
                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                // Muestra la imagen seleccionada en el ImageView
                imageViewSeleccionar.setImageURI(uri)
            }
        }
    }
    //Método para validar los campos y guardar la película en la base de datos
    private fun guardarPelicula() {
        //Obtiene el texto de los campos
        val titulo = editTextTitulo.text.toString().trim()
        val genero = editTextGenero.text.toString().trim()
        val anioStr = editTextAnio.text.toString().trim()

        //Verifica que todos los campos estén completos
        if (titulo.isEmpty() || genero.isEmpty() || anioStr.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }
        //Intenta convertir el año a entero, muestra error si no es válido
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
            //Regresa al hilo principal para mostrar un mensaje y cerrar la actividad
            runOnUiThread {
                Toast.makeText(
                    this@AgregarPeliculaActivity,
                    "Película guardada con éxito",
                    Toast.LENGTH_SHORT
                ).show()
                finish() //Cierra esta pantalla y vuelve a la anterior
            }
        }
    }
}
