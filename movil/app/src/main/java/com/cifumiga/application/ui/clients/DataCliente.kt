package com.cifumiga.application.ui.clients

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cifumiga.application.ui.services.AdaptadorServicios
import com.cifumiga.application.ui.services.AddServicio
import com.cifumiga.application.R
import com.cifumiga.application.models.ServicioX
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_data_cliente.*

class DataCliente : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var dbl : FirebaseFirestore
    private lateinit var servRecyclerView: RecyclerView
    private lateinit var servArrayList: ArrayList<ServicioX>
    private lateinit var adapter : AdaptadorServicios
    var llenarLista = ArrayList<ServicioX>()
    var nombre:String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_cliente)

        val bundle: Bundle?=intent.extras
        nombre = bundle?.getString("nombre")
        nombre_cliente.setText(bundle?.getString("nombre"))
        this.title = bundle?.getString("nombre")
        ruc_cliente.setText(bundle?.getString("ruc"))
        txtContactoOp.setText(bundle?.getString("contacto"))
        txtTelefonoOp.setText(bundle?.getString("telefono"))
        txtCorreoOp.setText(bundle?.getString("correo"))

        servRecyclerView = findViewById(R.id.lista_servicios)
        servRecyclerView.layoutManager = LinearLayoutManager(this)
        servRecyclerView.setHasFixedSize(true)
        servArrayList = arrayListOf()
        adapter = AdaptadorServicios(servArrayList)
        servRecyclerView.adapter = adapter


        btnAddService.setOnClickListener(){
            val intent = Intent(this, AddServicio::class.java)
            intent.putExtra("cliente", nombre)
            startActivity(intent)
        }


        btnDeleteClient.setOnClickListener(){
            alertEliminar(getString(R.string.titulo_alerta_seguridad),
                getString(R.string.mensaje_eliminar_registro))
        }

        getServices()


    }

    private fun getServices() {
        dbl = FirebaseFirestore.getInstance()
        dbl.collection("servicios").whereEqualTo("cliente", nombre).
        addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null){
                    showError("No se logró")
                    return
                }
                for (dc : DocumentChange in value?.documentChanges!!){
                    if (dc.type == DocumentChange.Type.ADDED){
                        servArrayList.add(dc.document.toObject(ServicioX::class.java))
                    }
                }
                adapter.notifyDataSetChanged()
            }

        })
    }


    private fun alertEliminar(t:String, s: String)  {
        val alertDialogBuilder = AlertDialog.Builder(this)
            .setTitle(t)
            .setMessage(s)
            .setPositiveButton("Si", { dialog, whichButton ->
                db.collection("clientes").document(nombre.toString()).delete()
                showError(getString(R.string.registro_eliminado))
            })
            .setNegativeButton("No", { dialog, whichButton ->
                dialog.dismiss()
            })
            .show()
    }

    private fun showError(s:String){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }
}