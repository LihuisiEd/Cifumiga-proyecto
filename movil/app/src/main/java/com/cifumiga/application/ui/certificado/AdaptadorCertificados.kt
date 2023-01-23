package com.cifumiga.application.ui.certificado

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cifumiga.application.R
import com.cifumiga.application.models.Certificado

class AdaptadorCertificados(var Lista: ArrayList<Certificado>):
    RecyclerView.Adapter<AdaptadorCertificados.ViewHolder>(){
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val ffecha_inicio = itemView.findViewById<TextView>(R.id.txtfecha_inicio)
        val ffecha_final = itemView.findViewById<TextView>(R.id.txtfecha_final)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.certificados_elements, parent, false);
        return ViewHolder(v);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.ffecha_inicio?.text= "Fecha de inicio: " + Lista[position].fecha_inicio
        holder?.ffecha_final?.text= "Fecha final: " + Lista[position].fecha_final
        val fecha_inicio = Lista[position].fecha_inicio
        val fecha_final = Lista[position].fecha_final
        val cliente = Lista[position].cliente
        holder?.itemView?.setOnClickListener(){
            val llamaractividad = Intent(holder.itemView.context, AddCertificado::class.java)
            llamaractividad.putExtra("fecha_inicio", fecha_inicio)
            llamaractividad.putExtra("fecha_final", fecha_final)
            llamaractividad.putExtra("cliente", cliente)
            holder.itemView.context.startActivity(llamaractividad)
        }
    }

    override fun getItemCount(): Int {
        return Lista.size
    }
}