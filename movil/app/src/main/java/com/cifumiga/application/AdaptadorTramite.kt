package com.cifumiga.application

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.cifumiga.application.models.Tramite

class AdaptadorTramite(var Lista:ArrayList<Tramite>): RecyclerView.Adapter<AdaptadorTramite.ViewHolder>(){
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val fnombre = itemView.findViewById<TextView>(R.id.txtClientNameT)
        val fcontacto = itemView.findViewById<TextView>(R.id.txtContactoT)
        val ffecha = itemView.findViewById<TextView>(R.id.txtFechaT)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.tramites_elements, parent, false);
        return ViewHolder(v);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.fnombre?.text=Lista[position].cliente
        holder?.ffecha?.text=Lista[position].tramite_fecha
        holder?.fcontacto?.text=Lista[position].tramite_contacto
    }

    override fun getItemCount(): Int {
        return Lista.size;
    }

    fun updateClientList(Lista:ArrayList<Tramite>){
        this.Lista = Lista
        notifyDataSetChanged()
    }

}