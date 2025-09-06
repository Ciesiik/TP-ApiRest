package com.example.dezero

import Elemento
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tp_apirest.R

class ElementosAdapter (var elementos: MutableList<Elemento>, var context: Context): RecyclerView.Adapter<ElementosAdapter.ElementoViewHolder>() {

    class ElementoViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val txtTitulo: TextView
        val txtAutor: TextView
        val txtFecha: TextView

        init{
            txtTitulo = view.findViewById(R.id.tvTitulo)
            txtFecha = view.findViewById(R.id.tvFecha)
            txtAutor = view.findViewById(R.id.tvAutor)

        }

    }

    override fun getItemCount() = elementos.size

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ElementoViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_elemento, viewGroup, false)

        return ElementoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ElementoViewHolder, position: Int){
        val item = elementos.get(position)
        holder.txtTitulo.text = item.titulo
        holder.txtAutor.text = item.autor
        holder.txtFecha.text = item.fecha
        holder.itemView.setOnClickListener(View.OnClickListener{
            Toast.makeText(context, item.titulo, Toast.LENGTH_SHORT).show() }
        )
    }

}