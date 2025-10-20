package com.example.tp_apirest.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tp_apirest.activities.track.TrackDetailsActivity
import com.example.tp_apirest.R
import com.example.tp_apirest.items.TrackItem

class TracksAdapter (
    var trackItems: List<TrackItem>,
    var context: Context,
    private val esFavorito: (Long) -> Boolean,
    private val onToggleFavorito: (TrackItem) -> Unit
): RecyclerView.Adapter<TracksAdapter.TrackItemViewHolder>() {

    class TrackItemViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val txtTitulo: TextView
        val txtAutor: TextView
        val txtDuracion: TextView
        var imgFavorito: ImageView
        var imgCover: ImageView

        init{
            txtTitulo = view.findViewById(R.id.tvTitulo)
            txtDuracion = view.findViewById(R.id.tvDuracion)
            txtAutor = view.findViewById(R.id.tvAutor)
            imgFavorito = view.findViewById(R.id.imgFavorito)
            imgCover = view.findViewById(R.id.imgCover)
        }

    }

    override fun getItemCount() = trackItems.size

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): TrackItemViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_track, viewGroup, false)

        return TrackItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackItemViewHolder, position: Int){
        val item = trackItems.get(position)
        holder.txtTitulo.text = item.titulo
        holder.txtAutor.text = item.autor
        holder.txtDuracion.text = item.duracion


        val favorito = esFavorito(item.id)
        val icono = if (favorito) R.drawable.favorito else R.drawable.no_favorito
        holder.imgFavorito.setImageResource(icono)

        holder.imgFavorito.setOnClickListener {
            onToggleFavorito(item)
        }

        Glide.with(context)
            .load(item.imagenUrl)
            .placeholder(R.drawable.placeholder)
            .into(holder.imgCover)


        holder.itemView.setOnClickListener {
            val contexto = holder.itemView.context
            val intent = Intent(contexto, TrackDetailsActivity::class.java)
            intent.putExtra("TRACK_ID", item.id)
            contexto.startActivity(intent)
        }

    }

    fun notifyItemChanged(trackId: Long) {
        val index = trackItems.indexOfFirst { it.id == trackId }
        if (index != -1) notifyItemChanged(index)
    }

}