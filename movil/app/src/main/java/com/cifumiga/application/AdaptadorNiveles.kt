package com.cifumiga.application

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cifumiga.application.models.Nivel

class AdaptadorNiveles(val Lista:ArrayList<Nivel>): RecyclerView.Adapter<AdaptadorNiveles.ViewHolder>(){
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val fop = itemView.findViewById<CheckBox>(R.id.op)
        val fnombre = itemView.findViewById<TextView>(R.id.txtNivel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.niveles_elements, parent, false);
        return ViewHolder(v);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.fnombre?.text=Lista[position].nserv_nombre
        if (holder.fop.isChecked()){

        }
    }

    override fun getItemCount(): Int {
        return Lista.size
    }
}