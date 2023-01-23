package com.cifumiga.application.ui.services

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cifumiga.application.R
import com.cifumiga.application.models.ServicioX

class AdaptadorServicios(var Lista: ArrayList<ServicioX>):
        RecyclerView.Adapter<AdaptadorServicios.ViewHolder>(){
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val ftipo = itemView.findViewById<TextView>(R.id.txtTipoServ)
        val farea = itemView.findViewById<TextView>(R.id.txtArea)
        val fdim = itemView.findViewById<TextView>(R.id.txtDim)
        val fEditr = itemView.findViewById<ImageButton>(R.id.btnEditService)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.servicios_elements, parent, false);
        return ViewHolder(v);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.ftipo?.text= Lista[position].tipo
        holder?.farea?.text= Lista[position].area
        holder?.fdim?.text= Lista[position].dimension + " m²"
        val id = Lista[position].id
        val desc = Lista[position].descripcion
        val area = Lista[position].area
        val dim = Lista[position].dimension
        val frec = Lista[position].frecuencia
        val precio = Lista[position].precio
        val tipo = Lista[position].tipo
        val cliente = Lista[position].cliente
        holder?.itemView?.setOnClickListener(){
            val llamaractividad = Intent(holder.itemView.context, DataServicio::class.java)
            llamaractividad.putExtra("desc", desc)
            llamaractividad.putExtra("area", area)
            llamaractividad.putExtra("dim", dim)
            llamaractividad.putExtra("frec", frec)
            llamaractividad.putExtra("precio", precio)
            llamaractividad.putExtra("cliente", cliente)
            llamaractividad.putExtra("tipo", tipo)
            holder.itemView.context.startActivity(llamaractividad)
        }
        holder?.fEditr?.setOnClickListener(){
            val llamaractividad = Intent(holder.itemView.context, UpdateServicio::class.java)
            llamaractividad.putExtra("id", id)
            llamaractividad.putExtra("desc", desc)
            llamaractividad.putExtra("area", area)
            llamaractividad.putExtra("dim", dim)
            llamaractividad.putExtra("frec", frec)
            llamaractividad.putExtra("precio", precio)
            llamaractividad.putExtra("cliente", cliente)
            holder.itemView.context.startActivity(llamaractividad)
        }
    }

    override fun getItemCount(): Int {
        return Lista.size
    }

}