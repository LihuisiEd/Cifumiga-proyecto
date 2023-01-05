package com.cifumiga.application

import android.app.DatePickerDialog
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.cifumiga.application.models.Nivel
import kotlinx.android.synthetic.main.activity_add_tramite.*
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddTramite : AppCompatActivity() {

    var cal = Calendar.getInstance()
    var id_cliente:Int? = null
    var id_tipo_servicio:Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tramite)

        val txtTipo = findViewById<AutoCompleteTextView>(R.id.txtTipoTram)

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
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

        val servicios = ArrayList<String>()
        val adapterTipos = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,servicios)
        AsyncTask.execute{
            val queue = Volley.newRequestQueue(this)
            val url = getString(R.string.urlAPI) + "/typeservice/"
            val stringRequest = JsonArrayRequest(url,
                Response.Listener{ response ->
                    try {
                        for (i in 0 until response.length()){
                            id_tipo_servicio = response.getJSONObject(i).getInt("id")
                            val servicio_nombre = response.getJSONObject(i).getString("servicio_nombre")
                            servicios.add(servicio_nombre)
                            txtTipo.setAdapter(adapterTipos)
                        }
                    } catch (e: JSONException) {
                        showError("Error al obtener los datos")
                    }
                }, Response.ErrorListener {
                    showError("Intenta ingresar mas tarde o revisa tu conexión a internet")
                })
            queue.add(stringRequest)
        }


        val txtClient = findViewById<AutoCompleteTextView>(R.id.txtClienTram)
        val clientes = ArrayList<String>()
        val adapterClientes = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, clientes)
        AsyncTask.execute{
            val queue = Volley.newRequestQueue(this)
            val url = getString(R.string.urlAPI) + "/clientes/"
            val stringRequest = JsonArrayRequest(url,
                Response.Listener{ response ->
                    try {
                        for (i in 0 until response.length()){
                            id_cliente = response.getJSONObject(i).getInt("id")
                            val cliente_nombre = response.getJSONObject(i).getString("cliente_nombre")
                            clientes.add(cliente_nombre)
                            txtClient.setAdapter(adapterClientes)
                        }
                    } catch (e: JSONException) {
                        showError("Error al obtener los datos")
                    }
                }, Response.ErrorListener {
                    showError("Intenta ingresar mas tarde o revisa tu conexión a internet")
                })
            queue.add(stringRequest)
        }

        txtClient.setOnItemClickListener{ adapterView, view, i, l ->
            id_cliente = i+1
        }

        txtTipo.setOnItemClickListener{ adapterView, view, i, l ->
            id_tipo_servicio = i+1
        }

        val bundle :Bundle ?=intent.extras
        val id_tramite = bundle?.getString("id").toString()
        if (bundle!=null){
            this.title = getString(R.string.modificar_tramite)
            txtFecTram.setText(bundle.getString("fecha").toString())
            txtContactTram.setText(bundle.getString("contacto").toString())
            txtTelefonoTram.setText(bundle.getString("telefono").toString())
            txtProblemaTram.setText(bundle.getString("problemas").toString())
            txtCondicionTram.setText(bundle.getString("condiciones").toString())
            txtDireccionTram.setText(bundle.getString("direccion").toString())
            txtReferenciaTram.setText(bundle.getString("referencia").toString())
            val nivel_1 = bundle.getString("nivel_1").toString()
            val nivel_2 = bundle.getString("nivel_2").toString()
            val nivel_3 = bundle.getString("nivel_3").toString()
            val nivel_4 = bundle.getString("nivel_4").toString()
            if (nivel_1 == "true"){
                op1.isActivated = true
            }
            if (nivel_2 == "true"){
                op2.isChecked = true
            }
            if (nivel_3 == "true"){
                op3.isChecked = true
            }
            if (nivel_4 == "true"){
                op4.isChecked = true
            }

            bdAddTramite.isEnabled = false
            bdUpdateTramite.isEnabled = true
        } else {
            this.title = getString(R.string.agregar_tramite)
            bdAddTramite.isEnabled = true
            bdUpdateTramite.isEnabled = false
        }

        bdAddTramite.setOnClickListener(){
            subirTramite()
        }

        bdUpdateTramite.setOnClickListener(){
            modTramite(id_tramite)
        }

    }

    private fun modTramite(id_tramite:String) {
        val cliente = txtClienTram.text.toString().trim()
        val tipo = txtTipoTram.text.toString().trim()
        val fecha = txtFecTram.text.toString().trim()
        val contacto = txtContactTram.text.toString().trim()
        val direccion = txtDireccionTram.text.toString().trim()
        val referencia = txtReferenciaTram.text.toString().trim()
        val telefono = txtTelefonoTram.text.toString().trim()
        val problema = txtProblemaTram.text.toString().trim()
        val condicion = txtCondicionTram.text.toString().trim()
        var nivel_1 = false
        var nivel_2 = false
        var nivel_3 = false
        var nivel_4 = false
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
        if (cliente.isEmpty() ){
            txtClienTram.error = "Especifica el cliente"
        }
        if (tipo.isEmpty()){
            txtTipoTram.error = "Especifica el tipo de servicio"
        }
        if (fecha.isEmpty() || contacto.isEmpty()|| telefono.isEmpty() ||
            problema.isEmpty() || direccion.isEmpty() || cliente.isEmpty() || tipo.isEmpty()){
            alertError("Asegúrate de llenar todos los campos")
        } else {
            val queue = Volley.newRequestQueue(this)
            val url = getString(R.string.urlAPI) + "/tramites/" + id_tramite
            val jsonObj = JSONObject()

            jsonObj.put("tramite_fecha", fecha )
            jsonObj.put("direccion", direccion)
            jsonObj.put("referencia", referencia)
            jsonObj.put("tramite_contacto", contacto)
            jsonObj.put("tramite_telefono", telefono)
            jsonObj.put("tramite_nivel_1", nivel_1)
            jsonObj.put("tramite_nivel_2", nivel_2)
            jsonObj.put("tramite_nivel_3", nivel_3)
            jsonObj.put("tramite_nivel_4", nivel_4)
            jsonObj.put("problemas", problema)
            jsonObj.put("condicion_subestandar", condicion)
            jsonObj.put("tipo", id_tipo_servicio )
            jsonObj.put("cliente", id_cliente)

            val stringRequest = JsonObjectRequest(
                Request.Method.PUT, url,jsonObj,
                Response.Listener { response ->
                    try {
                        showError("Trámite actualizado con éxito")
                    } catch (e: JSONException){
                        showError("Datos incorrectos")
                    }
                }, Response.ErrorListener {
                    showError("Problemas de conexión, revisa tu conexión a internet")
                })
            queue.add(stringRequest)

        }
    }

    private fun subirTramite() {
        val fecha = txtFecTram.text.toString().trim()
        val contacto = txtContactTram.text.toString().trim()
        val direccion = txtDireccionTram.text.toString().trim()
        val referencia = txtReferenciaTram.text.toString().trim()
        val telefono = txtTelefonoTram.text.toString().trim()
        val problema = txtProblemaTram.text.toString().trim()
        val condicion = txtCondicionTram.text.toString().trim()
        var nivel_1 = false
        var nivel_2 = false
        var nivel_3 = false
        var nivel_4 = false
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
        if (fecha.isEmpty() || contacto.isEmpty()|| telefono.isEmpty() || problema.isEmpty() || direccion.isEmpty()){
            showError("Debes llenar todos los datos del formulario!")
        } else {
            val queue = Volley.newRequestQueue(this)
            val url = getString(R.string.urlAPI) + "/tramites/"
            val jsonObj = JSONObject()

            jsonObj.put("tramite_fecha", fecha )
            jsonObj.put("direccion", direccion)
            jsonObj.put("referencia", referencia)
            jsonObj.put("tramite_contacto", contacto)
            jsonObj.put("tramite_telefono", telefono)
            jsonObj.put("tramite_nivel_1", nivel_1)
            jsonObj.put("tramite_nivel_2", nivel_2)
            jsonObj.put("tramite_nivel_3", nivel_3)
            jsonObj.put("tramite_nivel_4", nivel_4)
            jsonObj.put("problemas", problema)
            jsonObj.put("condicion_subestandar", condicion)
            jsonObj.put("tipo", id_tipo_servicio )
            jsonObj.put("cliente", id_cliente)

            val stringRequest = JsonObjectRequest(
                Request.Method.POST, url,jsonObj,
                Response.Listener { response ->
                    try {
                        showError("Támite agregado con éxito")
                    } catch (e: JSONException){
                        showError("Datos incorrectos")
                    }
                }, Response.ErrorListener {
                    showError("Problemas de conexión, revisa tu conexión a internet")
                })
            queue.add(stringRequest)

        }

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
        val myFormat = "yyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        txtFecTram.setText(sdf.format(cal.getTime()))
    }


    private fun showError(s:String){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }

}