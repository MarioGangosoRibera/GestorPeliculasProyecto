package com.example.gestiondepeliculasproyecto.ui

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gestiondepeliculasproyecto.EditarPeliculaActivity
import com.example.gestiondepeliculasproyecto.R
import com.example.gestiondepeliculasproyecto.modelo.Pelicula

//Adaptador para el RecyclerView que muestra la lista de peliculas
class AdaptadorPelicula (
    private val onBorrarClick: (Pelicula) -> Unit //Funcion al hacer clic en el boton borrar
): RecyclerView.Adapter<AdaptadorPelicula.PeliculaViewHolder>() {

    private var listaPeliculas = emptyList<Pelicula>() //Lista de peliculas

    // Crea la vista para cada elemento de la lista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeliculaViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_pelicula, parent, false)
        return PeliculaViewHolder(vista)
    }
    // Asigna los datos de cada pelicula a su vista
    override fun onBindViewHolder(holder: PeliculaViewHolder, position: Int) {
        val peliculaActual = listaPeliculas[position]
        holder.tituloTextView.text = peliculaActual.titulo
        holder.generoTextView.text = peliculaActual.genero
        holder.anioTextView.text = peliculaActual.anio.toString()

        val uri = peliculaActual.imagenUri
        if (!uri.isNullOrEmpty()) {
            try {
                holder.imagenPelicula.setImageURI(Uri.parse(uri))
            } catch (e: Exception) {
                holder.imagenPelicula.setImageResource(R.drawable.baseline_hide_image_24)
            }
        } else {
            holder.imagenPelicula.setImageResource(R.drawable.baseline_hide_image_24)
        }

        //Al hacer clic en el boton de borrar
        holder.botonBorrar.setOnClickListener {
            onBorrarClick(peliculaActual)
        }

        //Listener para abrir la pantalla de edicion al pulsar la card
        holder.itemView.setOnClickListener{
            val context = holder.itemView.context
            val intent = android.content.Intent(context, EditarPeliculaActivity::class.java).apply {
                putExtra("id", peliculaActual.id) // Asumiendo que Pelicula tiene un id
                putExtra("titulo", peliculaActual.titulo)
                putExtra("genero", peliculaActual.genero)
                putExtra("anio", peliculaActual.anio)
                putExtra("imageUri", peliculaActual.imagenUri)
            }
            context.startActivity(intent)
        }
    }
    // Método para obtener el tamaño de la lista
    override fun getItemCount(): Int {
        return listaPeliculas.size
    }
    // Actualiza la lista de películas que se va a mostrar
    fun setPeliculas(peliculas: List<Pelicula>) {
        this.listaPeliculas = peliculas
        notifyDataSetChanged() // Notifica al adaptador que los datos han cambiado
    }
    // ViewHolder que representa la vista de cada película
    class PeliculaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tituloTextView: TextView = itemView.findViewById(R.id.text_view_titulo)
        val generoTextView: TextView = itemView.findViewById(R.id.text_view_genero)
        val anioTextView: TextView = itemView.findViewById(R.id.text_view_anio)
        val botonBorrar: ImageButton = itemView.findViewById(R.id.boton_borrar)
        val imagenPelicula: ImageView = itemView.findViewById(R.id.image_view_pelicula)
    }
}