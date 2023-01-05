package com.cifumiga.application

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cifumiga.application.models.Kilometraje

class AdaptadorKilometraje(var Lista: ArrayList<Kilometraje>):
    RecyclerView.Adapter<AdaptadorKilometraje.ViewHolder>(){
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val ffecha = itemView.findViewById<TextView>(R.id.eleFechaKM)
        val fplaca = itemView.findViewById<TextView>(R.id.elePlacaKM)
        val finicio = itemView.findViewById<TextView>(R.id.eleInicioKM)
        val ffin = itemView.findViewById<TextView>(R.id.eleFinKM)
        val ftotal = itemView.findViewById<TextView>(R.id.eleTotalKM)
        val fbtnEdit = itemView.findViewById<ImageButton>(R.id.btnEditKM)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.kilometrajes_elements, parent, false);
        return ViewHolder(v);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.ffecha?.text= "Fecha: " + Lista[position].fecha
        holder?.fplaca?.text = "Placa: " + Lista[position].placa
        holder?.finicio?.text="Kilometraje Inicio: " + Lista[position].kilometraje_inicio
        holder?.ffin?.text="Kilometraje Fin: " + Lista[position].kilometraje_fin
        holder?.ftotal?.text="Kilometraje Total: " + Lista[position].kilometraje_total
        val id = Lista[position].id
        val fecha = Lista[position].fecha
        val inicio = Lista[position].kilometraje_inicio
        val fin = Lista[position].kilometraje_fin
        val placa = Lista[position].placa
        val total = Lista[position].kilometraje_total
        val empleado = Lista[position].empleado

        holder?.fbtnEdit?.setOnClickListener(){
            val llamaractividad = Intent(holder.itemView.context, UpdateKM::class.java)
            llamaractividad.putExtra("id", id)
            llamaractividad.putExtra("fecha", fecha)
            llamaractividad.putExtra("inicio", inicio)
            llamaractividad.putExtra("fin", fin)
            llamaractividad.putExtra("placa", placa)
            llamaractividad.putExtra("total", total)
            llamaractividad.putExtra("empleado", empleado)
            holder.itemView.context.startActivity(llamaractividad)
        }
    }

    override fun getItemCount(): Int {
        return Lista.size
    }

}