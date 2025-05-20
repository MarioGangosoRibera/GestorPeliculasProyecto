package com.example.gestiondepeliculasproyecto.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gestiondepeliculasproyecto.R
import com.example.gestiondepeliculasproyecto.modelo.Pelicula

class AdaptadorPelicula : RecyclerView.Adapter<AdaptadorPelicula.PeliculaViewHolder>() {
    private var listaPeliculas = emptyList<Pelicula>()
    // Método para crear la vista del ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeliculaViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_pelicula, parent, false)
        return PeliculaViewHolder(vista)
    }
    // Método para vincular los datos a la vista
    override fun onBindViewHolder(holder: PeliculaViewHolder, position: Int) {
        val peliculaActual = listaPeliculas[position]
        holder.tituloTextView.text = peliculaActual.titulo
        holder.generoTextView.text = peliculaActual.genero
        holder.anioTextView.text = peliculaActual.anio.toString()
    }
    // Método para obtener el tamaño de la lista
    override fun getItemCount(): Int {
        return listaPeliculas.size
    }
    // Método para actualizar la lista de películas
    fun setPeliculas(peliculas: List<Pelicula>) {
        this.listaPeliculas = peliculas
        notifyDataSetChanged() // Notifica al adaptador que los datos han cambiado
    }
    // Clase interna para el ViewHolder
    class PeliculaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tituloTextView: TextView = itemView.findViewById(R.id.text_view_titulo)
        val generoTextView: TextView = itemView.findViewById(R.id.text_view_genero)
        val anioTextView: TextView = itemView.findViewById(R.id.text_view_anio)
    }
}