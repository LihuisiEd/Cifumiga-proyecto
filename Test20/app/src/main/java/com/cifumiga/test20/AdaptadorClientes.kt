package com.cifumiga.test20

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cifumiga.test20.models.Cliente

class AdaptadorClientes(val Lista: ArrayList<Cliente>) : RecyclerView.Adapter<AdaptadorClientes.ViewHolder>() {

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val fNombre = itemView.findViewById<TextView>(R.id.eleClientName)
        val fRuc = itemView.findViewById<TextView>(R.id.eleClientRuc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.clients_elements, parent, false);
        return ViewHolder(v);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.fNombre?.text=Lista[position].cliente_nombre
        holder?.fRuc?.text=Lista[position].cliente_ruc
        //var nombre = Lista[position].cliente_nombre
    }

    override fun getItemCount(): Int {
        return Lista.size
    }

}
