package com.example.tp_apirest.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tp_apirest.R
import com.example.tp_apirest.activities.artist.ArtistDetailsActivity
import com.example.tp_apirest.items.ArtistItem
import de.hdodenhof.circleimageview.CircleImageView

class ArtistsAdapter(
    var artistItems: List<ArtistItem>,
    var context: Context
): RecyclerView.Adapter<ArtistsAdapter.ArtistItemViewHolder>(){

    class ArtistItemViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val txtNombre: TextView
        val txtPosicion: TextView
        val imgArtista: CircleImageView

        init{
            txtNombre = view.findViewById(R.id.tvNombre)
            txtPosicion = view.findViewById(R.id.tvPosicion)
            imgArtista = view.findViewById(R.id.civFoto)
        }
    }

    override fun getItemCount() = artistItems.size

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ArtistItemViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_artist, viewGroup, false)

        return ArtistItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArtistItemViewHolder, position: Int) {
        val item = artistItems.get(position)
        holder.txtNombre.text = item.nombre
        holder.txtPosicion.text = item.posicion.toString()
        Glide.with(context)
            .load(item.fotoUrl)
            .placeholder(R.drawable.placeholder)
            .into(holder.imgArtista)

        holder.itemView.setOnClickListener {
            val contexto = holder.itemView.context
            val intent = Intent(contexto, ArtistDetailsActivity::class.java)
            intent.putExtra("ARTIST_ID", item.id)
            contexto.startActivity(intent)
        }

    }

}