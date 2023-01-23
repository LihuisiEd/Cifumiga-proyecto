package com.cifumiga.application.ui.kilometraje

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cifumiga.application.R
import com.cifumiga.application.models.Kilometraje

import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_kilometraje.*

class KilometrajeActivity : AppCompatActivity() {

    private lateinit var db : FirebaseFirestore
    private lateinit var kmRecyclerView: RecyclerView
    private lateinit var kmArrayList: ArrayList<Kilometraje>
    private lateinit var adapter : AdaptadorKilometraje

    var email:String? = ""
    var permiso: String ?= ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kilometraje)



        val bundle: Bundle ? = intent.extras
        email = bundle?.getString("email")
        permiso = bundle?.getString("permiso")

        btnAddKM.setOnClickListener(){
            val intent = Intent(this, AddKm::class.java)
            intent.putExtra("email", email)
            startActivity(intent)
        }

        kmRecyclerView = findViewById(R.id.lista_kilometrajes)
        kmRecyclerView.layoutManager = LinearLayoutManager(this)
        kmRecyclerView.setHasFixedSize(true)
        kmArrayList = arrayListOf()
        adapter = AdaptadorKilometraje(kmArrayList)

        kmRecyclerView.adapter = adapter

        if (permiso != "admin"){
            getKmData()
        } else {
            getKmADMIN()
        }


    }

    private fun getKmADMIN() {
        db = FirebaseFirestore.getInstance()
        db.collection("kilometrajes").
        addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null){
                    showError("No se logró")
                    return
                }
                for (dc : DocumentChange in value?.documentChanges!!){
                    if (dc.type == DocumentChange.Type.ADDED){
                        kmArrayList.add(dc.document.toObject(Kilometraje::class.java))
                    }
                }
                adapter.notifyDataSetChanged()
            }

        })
    }

    private fun getKmData() {
        db = FirebaseFirestore.getInstance()
        db.collection("kilometrajes").whereEqualTo("empleado", email).
        addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null){
                    showError("No se logró")
                    return
                }
                for (dc : DocumentChange in value?.documentChanges!!){
                    if (dc.type == DocumentChange.Type.ADDED){
                        kmArrayList.add(dc.document.toObject(Kilometraje::class.java))
                    }
                }
                adapter.notifyDataSetChanged()
            }

        })

    }

    private fun showError(s:String){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }
}