package com.cifumiga.application.ui.clients

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cifumiga.application.AdaptadorClientes
import com.cifumiga.application.R
import com.cifumiga.application.models.Cliente
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_clientes.*

class ClientesActivity : AppCompatActivity() {

    private lateinit var db : FirebaseFirestore
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userArrayList: ArrayList<Cliente>
    private lateinit var adapter : AdaptadorClientes


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clientes)

        val mensaje = mensaje_busqueda


        btnAddClient.setOnClickListener() { val intent = Intent(this, AddClient::class.java)
            startActivity(intent) }

        userRecyclerView = findViewById(R.id.lista_clientes)

        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.setHasFixedSize(true)
        userArrayList = arrayListOf()
        adapter = AdaptadorClientes(userArrayList)

        userRecyclerView.adapter = adapter

        txtClientSearch.addTextChangedListener{
            clientFilter ->
            val llenarlistaClientes = userArrayList.filter {
                cliente -> cliente.nombre?.lowercase().toString().contains(clientFilter.toString().lowercase())
            }
            adapter.updateClientList(llenarlistaClientes as ArrayList<Cliente>)
            if (llenarlistaClientes.isEmpty()){
                mensaje.visibility = View.VISIBLE
            } else {
                mensaje.visibility = View.GONE
            }
        }


        getClientsData()



    }

    private fun getClientsData() {
        db = FirebaseFirestore.getInstance()
        db.collection("clientes").orderBy("nombre", Query.Direction.ASCENDING).
        addSnapshotListener(object : EventListener<QuerySnapshot>{
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null){
                    showError("No se logró")
                    return
                }
                for (dc : DocumentChange in value?.documentChanges!!){
                    if (dc.type == DocumentChange.Type.ADDED){
                        userArrayList.add(dc.document.toObject(Cliente::class.java))
                    }
                }
                adapter.notifyDataSetChanged()
            }

        })


    }

    private fun swipeconfig(swipe: SwipeRefreshLayout) {
        swipe.isEnabled = true
        swipe.isRefreshing = true
    }

    private fun swipeEnd(swipe: SwipeRefreshLayout) {
        swipe.isRefreshing = false
        swipe.isEnabled = false
    }

    private fun showError(s:String){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }
}