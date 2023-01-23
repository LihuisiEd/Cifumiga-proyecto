package com.cifumiga.application.ui.services


import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

import com.cifumiga.application.R
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import kotlinx.android.synthetic.main.activity_add_servicio.*

import org.json.JSONException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddServicio : AppCompatActivity() {

    private lateinit var db : FirebaseFirestore
    var nombre:String? = null
    var id_tipo:Int? = null
    var cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_servicio)

        val txtTipo = findViewById<Spinner>(R.id.txtTipoServ)
        val servicios = ArrayList<String>()
        val adapterTipos = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,servicios)
        db = FirebaseFirestore.getInstance()
        db.collection("tipos").
        addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null){
                    showError("No se logró")
                    return
                }
                for (dc : DocumentChange in value?.documentChanges!!){
                    if (dc.type == DocumentChange.Type.ADDED){
                        servicios.add(dc.document.get("nombre") as String)
                    }
                }
                adapterTipos.notifyDataSetChanged()
            }

        })
        txtTipo.adapter = adapterTipos

        txtTipoOtroServ.visibility = View.GONE
        txtTipoServ.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 == 5){
                    txtTipoOtroServ.visibility = View.VISIBLE
                } else {
                    txtTipoOtroServ.visibility = View.GONE
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }





        val bundle: Bundle? ?=intent.extras
        nombre = bundle?.getString("cliente")


        bdAddServicio.setOnClickListener(){
            addService()
        }



    }


    fun addService() {
        val id = "$nombre-${generarId()}"
        val area = txtAreaServ.text.toString().trim()
        val dim = txtDimServ.text.toString().trim()
        val frec = txtFrecServ.text.toString().trim()
        val precio = txtPrecServ.text.toString().trim()
        val desc = txtDescServ.text.toString().trim()
        var tipo = txtTipoServ.selectedItem.toString()
        if (tipo.equals("Otro")){
            tipo = txtTipoOtroServ.text.toString()
        }
        if (area.isEmpty() || dim.isEmpty()){
            showError("Debe llenar área y dimensión")
        } else {
            db.collection("servicios").document(id)
                .set(hashMapOf(
                    "id" to id,
                    "descripcion" to desc,
                    "area" to area,
                    "dimension" to dim,
                    "frecuencia" to frec,
                    "precio" to precio,
                    "tipo" to tipo,
                    "cliente" to nombre
                ))
                .addOnSuccessListener { showError("Guardado con éxito") }
                .addOnFailureListener { showError("Problemas al guardar") }
        }
    }

    private fun generarId(): String{
        val charset = ('A'..'Z') + ('0'..'9')

        return List(3) { charset.random() }
            .joinToString("")
    }
    private fun showError(s:String){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }
}