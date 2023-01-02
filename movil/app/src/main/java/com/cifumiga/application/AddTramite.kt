package com.cifumiga.application

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
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
import java.util.*
import kotlin.collections.ArrayList

class AddTramite : AppCompatActivity() {
    var mes:Int? = null
    var id_cliente:Int? = null
    var id_tipo_servicio:Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tramite)

        val dias = ArrayList<String>()
        for (i in 1 until 10) {
            dias.add("0"+i.toString())
        }
        for (i in 10 until 32) {
            dias.add(i.toString())
        }
        val adapterDias = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, dias)
        txtFecDayTram.setAdapter(adapterDias)

        val meses = ArrayList<String>()
        meses.add("Enero")
        meses.add("Febrero")
        meses.add("Marzo")
        meses.add("Abril")
        meses.add("Mayo")
        meses.add("Junio")
        meses.add("Julio")
        meses.add("Agosto")
        meses.add("Septiembre")
        meses.add("Octubre")
        meses.add("Noviembre")
        meses.add("Diciembre")
        val adapterMeses = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, meses)
        txtFecMounthTram.setAdapter(adapterMeses)

        txtFecMounthTram.setOnItemClickListener { adapterView, view, i ,l ->
            mes = i+1
        }


        val año = Calendar.getInstance().get(Calendar.YEAR);
        val años = ArrayList<String>()
        for (i in 1950 until año+1) {
            años.add(i.toString())
        }
        Collections.reverse(años)
        val adapterAños = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, años)
        txtFecYearTram.setAdapter(adapterAños)

        val txtTipo = findViewById<AutoCompleteTextView>(R.id.txtTipoTram)

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

        bdAddTramite.setOnClickListener(){
            subirTramite()
        }

        bdUpdateTramite.setOnClickListener(){
            modTramite()
        }

    }

    private fun modTramite() {
        TODO("Not yet implemented")
    }

    private fun subirTramite() {
        val fecha =txtFecYearTram.text.toString().trim() + "-" +
                mes + "-" + txtFecDayTram.text.toString().trim()
        val cliente = txtClienTram.text.toString().trim()
        val contacto = txtPhoneTram.text.toString().trim()
        val tipo_servicio = txtTipoTram.text.toString().trim()
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
        val descripcion = txtDescTram.text.toString().trim()

        if (fecha.isEmpty() || cliente.isEmpty() || contacto.isEmpty() || tipo_servicio.isEmpty() || descripcion.isEmpty()){
            showError("Debes llenar todos los datos del formulario!")
        } else {
            val queue = Volley.newRequestQueue(this)
            val url = getString(R.string.urlAPI) + "/tramites/"
            val jsonObj = JSONObject()

            jsonObj.put("tramite_fecha", fecha )
            jsonObj.put("tramite_contacto", contacto)
            jsonObj.put("tramite_nivel_1", nivel_1)
            jsonObj.put("tramite_nivel_2", nivel_2)
            jsonObj.put("tramite_nivel_3", nivel_3)
            jsonObj.put("tramite_nivel_4", nivel_4)
            jsonObj.put("tramite_descripcion", descripcion)
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


    private fun showError(s:String){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }

}