package com.cifumiga.application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.cifumiga.application.R
import kotlinx.android.synthetic.main.activity_add_client.*
import org.json.JSONException
import org.json.JSONObject

class AddClient : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_client)

        val contacto = txtAddContacto.text.toString().trim()
        val telefono = txtAddTelefono.text.toString().trim()
        val correo = txtAddCorreo.text.toString().trim()

        val bundle :Bundle ?=intent.extras
        val id_cliente = bundle?.getString("id").toString()
        if(bundle!=null){
            this.setTitle("Editar Cliente");
            txtAddRuc.setText(bundle.getString("ruc").toString())
            txtAddName.setText(bundle.getString("nombre").toString())
            if (contacto.isEmpty()){
                txtAddContacto.setText("Sin contacto")
            }

            if (telefono.isEmpty()){
                txtAddTelefono.setText("Sin telefono")
            }

            if (correo.isEmpty()){
                txtAddCorreo.setText("Sin correo")
            }
            txtAddContacto.setText(bundle.getString("contacto").toString())
            txtAddTelefono.setText(bundle.getString("telefono").toString())
            txtAddCorreo.setText(bundle.getString("correo").toString())
            bdAddClient.setEnabled(false)
            bdUpdateClient.setEnabled(true)
        } else{
            this.setTitle("Agregar Cliente");
            bdAddClient.setEnabled(true)
            bdUpdateClient.setEnabled(false)
        }


        bdAddClient.setOnClickListener(){
            subirCliente()
        }

        bdUpdateClient.setOnClickListener(){
            modCliente(id_cliente)
        }


    }

    private fun modCliente(id:String) {
        val ruc = txtAddRuc.text.toString().trim()
        val nombre = txtAddName.text.toString().trim()
        val contacto = txtAddContacto.text.toString().trim()
        val telefono = txtAddTelefono.text.toString().trim()
        val correo = txtAddCorreo.text.toString().trim()


        if (ruc.isEmpty() || nombre.isEmpty()){
            txtAddRuc.error = "Espacio requerido"
            txtAddName.error = "Espacio requerido"
        }
        if (ruc.isEmpty()){
            txtAddRuc.error = "Espacio requerido"
        }
        if (nombre.isEmpty()){
            txtAddName.error = "Espacio requerido"
        } else {
            val queue = Volley.newRequestQueue(this)
            val url = getString(R.string.urlAPI) + "/clientes/" + id
            val jsonObj = JSONObject()

            jsonObj.put("cliente_nombre", nombre )
            jsonObj.put("cliente_ruc", ruc)
            jsonObj.put("cliente_contacto", contacto)
            jsonObj.put("cliente_telefono", telefono)
            jsonObj.put("cliente_correo", correo)


            val stringRequest = JsonObjectRequest(
                Request.Method.PUT, url,jsonObj,
                Response.Listener { response ->
                    try {
                        showError("Cliente modificado con éxito")
                    } catch (e: JSONException){
                        showError("Datos incorrectos")
                    }
                }, Response.ErrorListener {
                    showError("Problemas de conexión, revisa tu conexión a internet")
                })
            queue.add(stringRequest)
        }

    }

    private fun subirCliente() {
        val ruc = txtAddRuc.text.toString().trim()
        val nombre = txtAddName.text.toString().trim()
        val contacto = txtAddContacto.text.toString().trim()
        val telefono = txtAddTelefono.text.toString().trim()
        val correo = txtAddCorreo.text.toString().trim()

        if (ruc.isEmpty() || nombre.isEmpty()){
            txtAddRuc.error = "Espacio requerido"
            txtAddName.error = "Espacio requerido"
        }
        if (ruc.isEmpty()){
            txtAddRuc.error = "Espacio requerido"
        }
        if (nombre.isEmpty()){
            txtAddName.error = "Espacio requerido"
        } else {
            val queue = Volley.newRequestQueue(this)
            val url = getString(R.string.urlAPI) + "/clientes/"
            val jsonObj = JSONObject()

            jsonObj.put("cliente_nombre", nombre )
            jsonObj.put("cliente_ruc", ruc)
            jsonObj.put("cliente_contacto", contacto)
            jsonObj.put("cliente_telefono", telefono)
            jsonObj.put("cliente_correo", correo)

            val stringRequest = JsonObjectRequest(
                Request.Method.POST, url,jsonObj,
                Response.Listener { response ->
                    try {
                        showError("Cliente agregado con éxito")
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