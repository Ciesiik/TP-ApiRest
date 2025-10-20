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
import com.example.tp_apirest.R
import com.example.tp_apirest.activities.album.AlbumDetailsActivity
import com.example.tp_apirest.items.AlbumItem

class AlbumsAdapter (
    var albumItems: List<AlbumItem>,
    var context: Context
): RecyclerView.Adapter<AlbumsAdapter.AlbumItemViewHolder>() {

    class AlbumItemViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val txtTitulo: TextView
        val txtAutor: TextView
        val txtPosicion: TextView
        val txtTipo: TextView
        val imgCover: ImageView

        init{
            txtTitulo = view.findViewById(R.id.tvTitulo)
            txtAutor = view.findViewById(R.id.tvAutor)
            txtPosicion = view.findViewById(R.id.tvPosicion)
            txtTipo = view.findViewById(R.id.tvAlbumTipo)
            imgCover = view.findViewById(R.id.ivPortada)
        }
    }

    override fun getItemCount() = albumItems.size

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AlbumItemViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_album, viewGroup, false)

        return AlbumItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlbumItemViewHolder, position: Int) {
        val item = albumItems.get(position)
        holder.txtTitulo.text = item.titulo
        holder.txtAutor.text = item.artista
        holder.txtTipo.text = item.tipo.uppercase()

        Glide.with(context)
            .load(item.coverUrl)
            .placeholder(R.drawable.placeholder)
            .into(holder.imgCover)

        holder.itemView.setOnClickListener {
            val contexto = holder.itemView.context
            val intent = Intent(contexto, AlbumDetailsActivity::class.java)

            intent.putExtra("ALBUM_ID", item.id)
            contexto.startActivity(intent)
        }
    }
}