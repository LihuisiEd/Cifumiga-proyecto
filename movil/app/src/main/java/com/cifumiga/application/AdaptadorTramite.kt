package com.cifumiga.application

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.cifumiga.application.models.Tramite

class AdaptadorTramite(var Lista:ArrayList<Tramite>): RecyclerView.Adapter<AdaptadorTramite.ViewHolder>(){
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val fnombre = itemView.findViewById<TextView>(R.id.txtClientNameT)
        val fcontacto = itemView.findViewById<TextView>(R.id.txtContactoT)
        val ffecha = itemView.findViewById<TextView>(R.id.txtFechaT)
        val feditar = itemView.findViewById<ImageButton>(R.id.btnEditClientT)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.tramites_elements, parent, false);
        return ViewHolder(v);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.fnombre?.text=Lista[position].cliente
        holder?.ffecha?.text=Lista[position].tramite_fecha
        holder?.fcontacto?.text=Lista[position].tramite_contacto
        val id = Lista[position].id
        val cliente = Lista[position].cliente
        val tipo = Lista[position].tipo
        val contacto = Lista[position].tramite_contacto
        val telefono = Lista[position].tramite_telefono
        val direccion = Lista[position].direccion
        val referencia = Lista[position].referencia
        val fecha = Lista[position].tramite_fecha
        val nivel_1 = Lista[position].tramite_nivel_1
        val nivel_2 = Lista[position].tramite_nivel_2
        val nivel_3 = Lista[position].tramite_nivel_3
        val nivel_4 = Lista[position].tramite_nivel_4
        val problemas = Lista[position].problemas
        val condiciones = Lista[position].condicion_subestandar
        holder.itemView.setOnClickListener(){
            val llamaractividad = Intent(holder.itemView.context, DataTramite::class.java)
            llamaractividad.putExtra("id",id.toString())
            llamaractividad.putExtra("cliente", cliente)
            llamaractividad.putExtra("tipo", tipo)
            llamaractividad.putExtra("contacto", contacto)
            llamaractividad.putExtra("telefono", telefono)
            llamaractividad.putExtra("direccion", direccion)
            llamaractividad.putExtra("referencia", referencia)
            llamaractividad.putExtra("fecha", fecha)
            llamaractividad.putExtra("nivel_1",nivel_1.toString())
            llamaractividad.putExtra("nivel_2",nivel_2.toString())
            llamaractividad.putExtra("nivel_3",nivel_3.toString())
            llamaractividad.putExtra("nivel_4",nivel_4.toString())
            llamaractividad.putExtra("problemas",problemas)
            llamaractividad.putExtra("condiciones",condiciones)
            holder.itemView.context.startActivity(llamaractividad)
        }
        holder.feditar.setOnClickListener(){
            val llamaractividad = Intent(holder.itemView.context, AddTramite::class.java)
            llamaractividad.putExtra("id",id.toString())
            llamaractividad.putExtra("cliente", cliente)
            llamaractividad.putExtra("tipo", tipo)
            llamaractividad.putExtra("contacto", contacto)
            llamaractividad.putExtra("telefono", telefono)
            llamaractividad.putExtra("direccion", direccion)
            llamaractividad.putExtra("referencia", referencia)
            llamaractividad.putExtra("fecha", fecha)
            llamaractividad.putExtra("nivel_1",nivel_1.toString())
            llamaractividad.putExtra("nivel_2",nivel_2.toString())
            llamaractividad.putExtra("nivel_3",nivel_3.toString())
            llamaractividad.putExtra("nivel_4",nivel_4.toString())
            llamaractividad.putExtra("problemas",problemas)
            llamaractividad.putExtra("condiciones",condiciones)
            holder.itemView.context.startActivity(llamaractividad)
        }
    }

    override fun getItemCount(): Int {
        return Lista.size;
    }

    fun updateClientList(Lista:ArrayList<Tramite>){
        this.Lista = Lista
        notifyDataSetChanged()
    }

}