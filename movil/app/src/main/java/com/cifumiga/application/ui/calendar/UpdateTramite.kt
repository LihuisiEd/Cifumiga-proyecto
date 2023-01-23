package com.cifumiga.application.ui.calendar

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.cifumiga.application.R
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import kotlinx.android.synthetic.main.activity_update_tramite.*
import java.text.SimpleDateFormat
import java.util.*

class UpdateTramite : AppCompatActivity() {

    private lateinit var db : FirebaseFirestore
    var cal = Calendar.getInstance()
    var cliente:String? = ""
    var tipo:String? = ""
    var frecuencia: String = ""
    var id : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_tramite)
        val txtTipo = txtTipoTramUp
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
        txtNumeroTramUp.isEnabled = false

        val txtFrecuencia = txtFrecuenciaTramiteUp
        val frecuencias = ArrayList<String>()
        val adapterFrec = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,frecuencias)
        db.collection("frecuencias").
        addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null){
                    showError("No se logró")
                    return
                }
                for (dc : DocumentChange in value?.documentChanges!!){
                    if (dc.type == DocumentChange.Type.ADDED){
                        frecuencias.add(dc.document.get("nombre") as String)
                    }
                }
                adapterFrec.notifyDataSetChanged()
            }

        })
        txtFrecuencia.adapter = adapterFrec

        val txtClient = txtClienTramUp
        val clientes = ArrayList<String>()
        val adapterClientes = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, clientes)
        db.collection("clientes").
        addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null){
                    showError("No se logró")
                    return
                }
                for (dc : DocumentChange in value?.documentChanges!!){
                    if (dc.type == DocumentChange.Type.ADDED){
                        clientes.add(dc.document.get("nombre") as String)
                    }
                }
                adapterClientes.notifyDataSetChanged()
            }

        })
        txtClient.adapter = adapterClientes


        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }

        btnFecTramUp.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(this@UpdateTramite,
                    dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }

        })

        txtTipoOtroTramUp.visibility = View.GONE
        txtTipoTramUp.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 == 5){
                    txtTipoOtroTramUp.visibility = View.VISIBLE
                } else {
                    txtTipoOtroTramUp.visibility = View.GONE
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        val bundle :Bundle ?=intent.extras
        id = bundle?.getString("id").toString()
        if (bundle!=null){
            this.title = getString(R.string.modificar_tramite)
            txtFecTramUp.setText(bundle.getString("fecha").toString())
            cliente = bundle.getString("cliente").toString()
            tipo = bundle.getString("tipo").toString()
            frecuencia = bundle.getString("frecuencia").toString()
            txtNumeroTramUp.setText(bundle.getString("id").toString())
            txtCulmTramUp.setText(bundle.getString("culminacion").toString())
            txtContactTramUp.setText(bundle.getString("contacto").toString())
            txtTelefonoTramUp.setText(bundle.getString("telefono").toString())
            txtProblemaTramUp.setText(bundle.getString("problemas").toString())
            txtCondicionTramUp.setText(bundle.getString("condiciones").toString())
            txtDireccionTramUp.setText(bundle.getString("direccion").toString())
            txtReferenciaTramUp.setText(bundle.getString("referencia").toString())
            val nivel_1 = bundle.getString("nivel_1").toString()
            val nivel_2 = bundle.getString("nivel_2").toString()
            val nivel_3 = bundle.getString("nivel_3").toString()
            val nivel_4 = bundle.getString("nivel_4").toString()
            if (nivel_1 == "true"){
                op1Up.isActivated = true
            }
            if (nivel_2 == "true"){
                op2Up.isChecked = true
            }
            if (nivel_3 == "true"){
                op3Up.isChecked = true
            }
            if (nivel_4 == "true"){
                op4Up.isChecked = true
            }

            txtTipoTramUp.isEnabled = false
            txtFrecuenciaTramiteUp.isEnabled = false
            txtClienTramUp.isEnabled = false

            bdAddTramiteUp.setOnClickListener(){
                subirTramite()
            }

        }
    }

    private fun subirTramite() {
        val nombre = txtClienTramUp.selectedItem.toString()
        val fecha_cul = txtCulmTramUp.text.toString().trim()
        val fecha = txtFecTramUp.text.toString().trim()

        var nivel_1 = false
        var nivel_2 = false
        var nivel_3 = false
        var nivel_4 = false
        if (op1Up.isChecked){
            nivel_1 = true
        }
        if (op2Up.isChecked){
            nivel_2 = true
        }
        if (op3Up.isChecked){
            nivel_3 = true
        }
        if (op4Up.isChecked){
            nivel_4 = true
        }
        if (txtFecTramUp.text.isEmpty()){
            showError("Debes colocar la fecha inicial")
            txtFecTramUp.error = "Campo requerido"
        } else {
            db.collection("hojas_trabajo").document("$id $fecha")
                .set(hashMapOf(
                    "id" to id,
                    "cliente" to cliente,
                    "tipo" to tipo,
                    "direccion" to txtDireccionTramUp.text.toString(),
                    "referencia" to txtReferenciaTramUp.text.toString(),
                    "contacto" to txtContactTramUp.text.toString(),
                    "telefono" to txtTelefonoTramUp.text.toString(),
                    "fecha" to txtFecTramUp.text.toString(),
                    "culminacion" to fecha_cul,
                    "frecuencia" to frecuencia,
                    "nivel_1" to nivel_1.toString(),
                    "nivel_2" to nivel_2.toString(),
                    "nivel_3" to nivel_3.toString(),
                    "nivel_4" to nivel_4.toString(),
                    "problemas" to txtProblemaTramUp.text.toString(),
                    "condicion" to txtCondicionTramUp.text.toString()

                ))
                .addOnSuccessListener { showError("Guardado con éxito") }
                .addOnFailureListener { showError("Problemas al guardar") }

        }

    }


    private fun updateDateInView() {
        val myFormat = "dd-MM-yyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        txtFecTramUp.setText(sdf.format(cal.getTime()))
    }


    private fun showError(s:String){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }
}