package com.cifumiga.application

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cifumiga.application.models.Operacion

class AdaptadorOperacion(var Lista: ArrayList<Operacion>): RecyclerView.Adapter<AdaptadorOperacion.ViewHolder>() {
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val fcontacto = itemView.findViewById<TextView>(R.id.txtContactoOp)
        val fcorreo = itemView.findViewById<TextView>(R.id.txtCorreoOp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.operaciones_elements, parent, false);
        return AdaptadorOperacion.ViewHolder(v);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.fcontacto?.text = Lista[position].operacion_contacto
        holder?.fcorreo?.text = Lista[position].operacion_correo
    }

    override fun getItemCount(): Int {
        return Lista.size
    }

}