package com.cifumiga.application

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cifumiga.application.models.Cliente
import com.cifumiga.application.ui.clients.AddClient
import com.cifumiga.application.ui.clients.DataCliente

class AdaptadorClientes(var Lista: ArrayList<Cliente>):
    RecyclerView.Adapter<AdaptadorClientes.ViewHolder>() {

    override fun getItemCount(): Int {
        return Lista.size
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val fNombre = itemView.findViewById<TextView>(R.id.eleClientName)
        val fRuc = itemView.findViewById<TextView>(R.id.eleClientRuc)
        val fEditar = itemView.findViewById<ImageButton>(R.id.btnEditClient)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.clients_elements, parent, false);
        return ViewHolder(v);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.fNombre?.text=Lista[position].nombre
        holder?.fRuc?.text=Lista[position].ruc

        var nombre = Lista[position].nombre
        var ruc = Lista[position].ruc
        var contacto = Lista[position].contacto
        var telefono = Lista[position].telefono
        var correo = Lista[position].correo

        holder.itemView.setOnClickListener{
            val llamaractividad = Intent(holder.itemView.context, DataCliente::class.java)
            llamaractividad.putExtra("nombre",nombre)
            llamaractividad.putExtra("ruc",ruc)
            llamaractividad.putExtra("contacto",contacto)
            llamaractividad.putExtra("telefono", telefono)
            llamaractividad.putExtra("correo",correo)
            holder.itemView.context.startActivity(llamaractividad)
        }

        holder.fEditar?.setOnClickListener{
            val llamaractividad = Intent(holder.itemView.context, AddClient::class.java)
            llamaractividad.putExtra("nombre",nombre)
            llamaractividad.putExtra("ruc",ruc)
            llamaractividad.putExtra("contacto",contacto)
            llamaractividad.putExtra("telefono", telefono)
            llamaractividad.putExtra("correo",correo)
            holder.itemView.context.startActivity(llamaractividad)
        }

    }

    fun updateClientList(Lista:ArrayList<Cliente>){
        this.Lista = Lista
        notifyDataSetChanged()
    }



}
