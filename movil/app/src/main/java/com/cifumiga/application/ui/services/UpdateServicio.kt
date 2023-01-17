package com.cifumiga.application.ui.services

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.cifumiga.application.R
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_update_servicio.*




class UpdateServicio : AppCompatActivity() {
    private lateinit var db : FirebaseFirestore
    var id_servicio:Int? = null
    var id_tipo:Int? = null
    var nombre:String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_servicio)

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

        txtTipoOtroServU.visibility = View.GONE
        txtTipoServ.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 == 2){
                    txtTipoOtroServU.visibility = View.VISIBLE
                } else {
                    txtTipoOtroServU.visibility = View.GONE
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }



        val bundle : Bundle?=intent.extras
        if (bundle!=null){
            nombre = bundle.getString("cliente")
            txtAreaServ.setText(bundle?.getString("area"))
            txtDimServ.setText(bundle?.getString("dim"))
            txtFrecServ.setText(bundle?.getString("frec"))
            txtPrecServ.setText(bundle?.getString("precio"))
            txtDescServ.setText(bundle?.getString("desc"))
            bdAddServicio.isEnabled = false
        }

        bdAddServicio.setOnClickListener(){
            updateServicio()
        }

    }

    fun updateServicio() {
        val area = txtAreaServ.text.toString().trim()
        val dim = txtDimServ.text.toString().trim()
        val frec = txtFrecServ.text.toString().trim()
        val precio = txtPrecServ.text.toString().trim()
        val desc = txtDescServ.text.toString().trim()
        var tipo = txtTipoServ.selectedItem.toString()
        if (tipo.equals("Otro")){
            tipo = txtTipoOtroServU.text.toString()
        }
        if (area.isEmpty() || dim.isEmpty() || frec.isEmpty() || precio.isEmpty() || desc.isEmpty()){
            showError("Todos los campos deben ser llenados")
        }
        if (tipo.isEmpty()){
            showError("Debes seleccionar el tipo de servicio")
        } else {
            val servicio = hashMapOf(
                "descripcion" to desc,
                "area" to area,
                "dimension" to dim,
                "frecuencia" to frec,
                "precio" to precio,
                "tipo" to tipo,
                "cliente" to nombre
            )
            db.collection("servicios").document(nombre.toString())
                .set(servicio)
                .addOnSuccessListener { showError("Guardado con éxito") }
                .addOnFailureListener { showError("Problemas al guardar") }
        }

    }

    private fun showError(s:String){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }
}