package com.cifumiga.application

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_add_servicio.*
import org.json.JSONException
import org.json.JSONObject

class AddServicio : AppCompatActivity() {

    var id_cliente:String? = null
    var id_tipo:Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_servicio)

        val txtTipo = findViewById<AutoCompleteTextView>(R.id.txtTipoServ)
        val servicios = ArrayList<String>()
        val adapterTipos = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,servicios)
        AsyncTask.execute{
            val queue = Volley.newRequestQueue(this)
            val url = getString(R.string.urlAPI) + "/typeservice/"
            val stringRequest = JsonArrayRequest(url,
                Response.Listener{ response ->
                    try {
                        for (i in 0 until response.length()){
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

        txtTipo.setOnItemClickListener{ adapterView, view, i, l ->
            id_tipo = i+1
        }

        val bundle: Bundle? ?=intent.extras
        id_cliente = bundle?.getString("id_cliente")
        bdUpdateServicio.isEnabled = false

        bdAddServicio.setOnClickListener(){
            addService()
        }



    }

    fun addService() {
        val area = txtAreaServ.text.toString().trim()
        val dim = txtDimServ.text.toString().trim()
        val frec = txtFrecServ.text.toString().trim()
        val precio = txtPrecServ.text.toString().trim()
        val desc = txtDescTram.text.toString().trim()
        val tipo = txtTipoServ.text.toString().trim()
        if (area.isEmpty() || dim.isEmpty() || frec.isEmpty() || precio.isEmpty() || desc.isEmpty()){
            showError("Todos los campos deben ser llenados")
        }
        if (tipo.isEmpty()){
            showError("Debes seleccionar el tipo de servicio")
        } else {
            val queue = Volley.newRequestQueue(this)
            val url = getString(R.string.urlAPI) + "/services/"
            val jsonObj = JSONObject()

            jsonObj.put("servicio_desc", desc)
            jsonObj.put("servicio_area", area)
            jsonObj.put("servicio_dim", dim)
            jsonObj.put("servicio_frec", frec)
            jsonObj.put("servicio_precio", precio)
            jsonObj.put("tipo", id_tipo?.toInt())
            jsonObj.put("cliente", id_cliente?.toInt())

            val stringRequest = JsonObjectRequest(
                Request.Method.POST, url, jsonObj,
                Response.Listener { response ->
                    try {
                        showError("Servicio agregado con éxito")
                    } catch (e: JSONException) {
                        showError("Datos incorrectos")
                    }
                }, Response.ErrorListener {
                    showError("Problemas de conexión, revisa tu conexión a internet")
                })
            queue.add(stringRequest)

        }
    }
    private fun showError(s:String){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }
}