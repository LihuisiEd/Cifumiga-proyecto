package com.cifumiga.application.ui.calendar

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.cifumiga.application.R
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import kotlinx.android.synthetic.main.activity_add_tramite.*
import java.text.SimpleDateFormat
import java.util.*


class AddTramite : AppCompatActivity() {

    private lateinit var db : FirebaseFirestore

    var cal = Calendar.getInstance()
    var cliente:String? = ""
    var tipo:String? = ""
    var problema:String? = ""
    var frecuencia: Int = 0
    var dias:Int = 0
    var fecha:String = ""
    var fecha_cul:String =""
    var fecha_id:String =  ""


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tramite)

        val txtTipo = findViewById<Spinner>(R.id.txtTipoTram)
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

        val txtFrecuencia = findViewById<Spinner>(R.id.txtFrecuenciaTramite)
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

        val txtClient = findViewById<Spinner>(R.id.txtClienTram)
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

        val dateSetListenerCul = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInViewCul()
            }
        }

        btnFecTram.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(this@AddTramite,
                    dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }

        })

        btnCulTram.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(this@AddTramite,
                    dateSetListenerCul,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }
        })

        txtTipoOtroTram.visibility = View.GONE
        txtFrecuenciaOtroTram.visibility = View.GONE
        txtTipoTram.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 == 5){
                    txtTipoOtroTram.visibility = View.VISIBLE
                } else {
                    txtTipoOtroTram.visibility = View.GONE
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        txtFrecuenciaTramite.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                //Semanal
                if (p2 == 0){
                    frecuencia = 52
                    dias = 7
                    txtFrecuenciaOtroTram.setText(dias.toString())
                }
                //Mensual
                if (p2 == 1){
                    frecuencia = 12
                    dias = 30
                    txtFrecuenciaOtroTram.setText(dias.toString())
                }
                //Trimestral
                if (p2 == 2){
                    frecuencia = 4
                    dias = 91
                    txtFrecuenciaOtroTram.setText(dias.toString())
                }
                //Semestral
                if (p2 == 3){
                    frecuencia = 2
                    dias = 183
                    txtFrecuenciaOtroTram.setText(dias.toString())
                }

                if (p2 == 4){
                    txtFrecuenciaOtroTram.visibility = View.VISIBLE
                    txtFrecuenciaOtroTram.setText(dias.toString())
                }


                /*
                Si es semanal ---> dias +7
                Si es Mensual ---> dias +30
                Si es Trimestral --> dias +91
                Si es Semestral ---> dias +183
                 */
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        bdAddTramite.setOnClickListener(){
            subirTramite()
        }


        this.title = getString(R.string.agregar_tramite)



    }


    private fun subirTramite() {
        fecha = txtFecTram.text.toString().trim()
        fecha_cul = txtCulmTram.text.toString().trim()
        var nombre = txtClienTram.selectedItem.toString()
        val id = "${nombre.uppercase()}-${generarId()}"
        var nivel_1 = false
        var nivel_2 = false
        var nivel_3 = false
        var nivel_4 = false
        var tipo = txtTipoTram.selectedItem.toString()
        var days_frec = txtFrecuenciaTramite.selectedItem.toString()
        if (tipo.equals("Otro")){
            tipo = txtTipoOtroTram.text.toString()
        }

        if (days_frec.equals("Otro")){
            dias = txtFrecuenciaOtroTram.text.toString().toInt()
        }
        if (op1.isChecked){
            nivel_1 = true
        }
        if (op2.isChecked){
            nivel_2 = true
        }
        if (op3.isChecked){
            nivel_3 = true
        }
        if (op4.isChecked){
            nivel_4 = true
        }
        if (fecha.isEmpty()){
            showError("Debes colocar la fecha inicial")
            txtFecTram.error = "Campo requerido"
        }
        if (fecha_cul.isEmpty()){
            showError("Debes colocar la fecha de culminación")
            txtCulmTram.error = "Campo requerido"
        } else {
            var termino = compruebaCulminacino(fecha)
            while(termino){
                termino = compruebaCulminacino(fecha)
                if (!termino){
                    break
                }
                db.collection("hojas_trabajo").document("$id ${sumarFecha(fecha, dias)}")
                    .set(hashMapOf(
                        "id" to id,
                        "cliente" to nombre,
                        "tipo" to tipo,
                        "direccion" to txtDireccionTram.text.toString(),
                        "referencia" to txtReferenciaTram.text.toString(),
                        "contacto" to txtContactTram.text.toString(),
                        "telefono" to txtTelefonoTram.text.toString(),
                        "fecha" to fecha,
                        "culminacion" to fecha_cul,
                        "frecuencia" to txtFrecuenciaTramite.selectedItem.toString(),
                        "nivel_1" to nivel_1.toString(),
                        "nivel_2" to nivel_2.toString(),
                        "nivel_3" to nivel_3.toString(),
                        "nivel_4" to nivel_4.toString(),
                        "problemas" to txtProblemaTram.text.toString(),
                        "condicion" to txtCondicionTram.text.toString()

                    ))
                    .addOnSuccessListener { showError("Guardado con éxito") }
                    .addOnFailureListener { showError("Problemas al guardar") }
                termino = compruebaCulminacino(fecha)
            }
        }
    }

    private fun compruebaCulminacino(fecha: String):Boolean {
        var validacion = false
        val fe = SimpleDateFormat("dd-MM-yyyy")
        val fechaInicio: Date = fe.parse(fecha) as Date
        val fechaFin: Date = fe.parse(fecha_cul) as Date
        //validacion = fecha_inicio.before(fecha_fin)
        if (fechaInicio.compareTo(fechaFin) > 0){
            validacion = false
        } else if (fechaInicio.compareTo(fechaFin) < 0){
            validacion = true
        }
        return validacion
    }

    private fun generarId(): String{
        val charset = ('A'..'Z') + ('0'..'9')

        return List(3) { charset.random() }
            .joinToString("")
    }

    private fun sumarFecha(fecha_dada:String, dias:Int):String{
        val f = fecha_dada.split("-")

        cal[f[2].toInt(), f[1].toInt() - 1] = f[0].toInt()

        cal.add(Calendar.DAY_OF_MONTH, dias)
        val fe = SimpleDateFormat("dd-MM-yyyy")
        fecha = fe.format(cal.getTime())
        return fe.format(cal.getTime())
    }

    private fun alertError(s: String) {
        val alertDialogBuilder = AlertDialog.Builder(this)
            .setTitle("Ops! Algo salió mal")
            .setMessage(s)
            .setPositiveButton("Ok", { dialog, whichButton ->
                dialog.dismiss()
            })
            .show()
    }

    private fun updateDateInView() {
        val myFormat = "dd-MM-yyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        txtFecTram.setText(sdf.format(cal.getTime()))
    }

    private fun updateDateInViewCul() {
        val myFormat = "dd-MM-yyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        txtCulmTram.setText(sdf.format(cal.getTime()))
    }


    private fun showError(s:String){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }

}