package com.cifumiga.application.ui.kilometraje

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cifumiga.application.R
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
        holder?.ffecha?.text= Lista[position].fecha.toString()
        holder?.fplaca?.text = Lista[position].placa
        holder?.finicio?.text="Inicial: " + Lista[position].inicial
        holder?.ffin?.text="Final: " + Lista[position].final
        holder?.ftotal?.text= Lista[position].total.toString()
        val fecha = Lista[position].fecha
        val inicio = Lista[position].inicial
        val fin = Lista[position].final
        val placa = Lista[position].placa
        val total = Lista[position].total
        val empleado = Lista[position].empleado

        holder?.itemView?.setOnClickListener(){
            val llamaractividad = Intent(holder.itemView.context, DataKilometraje::class.java)
            llamaractividad.putExtra("fecha", fecha)
            llamaractividad.putExtra("inicio", inicio)
            llamaractividad.putExtra("fin", fin)
            llamaractividad.putExtra("placa", placa)
            llamaractividad.putExtra("total", total)
            holder.itemView.context.startActivity(llamaractividad)
        }

        holder?.fbtnEdit?.setOnClickListener(){
            val llamaractividad = Intent(holder.itemView.context, UpdateKM::class.java)
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