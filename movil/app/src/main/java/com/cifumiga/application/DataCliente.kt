package com.cifumiga.application

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.cifumiga.application.models.ServicioX
import com.cifumiga.application.ui.calendar.CalendarActivity
import kotlinx.android.synthetic.main.activity_data_cliente.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException

class DataCliente : AppCompatActivity() {

    var llenarLista = ArrayList<ServicioX>()
    var id_cliente:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_cliente)

        val bundle: Bundle?=intent.extras
        id_cliente = bundle?.getString("id")
        nombre_cliente.setText(bundle?.getString("nombre"))
        this.title = bundle?.getString("nombre")
        ruc_cliente.setText(bundle?.getString("ruc"))
        txtContactoOp.setText(bundle?.getString("contacto"))
        txtTelefonoOp.setText(bundle?.getString("telefono"))
        txtCorreoOp.setText(bundle?.getString("correo"))

        btnAddService.setOnClickListener(){
            val intent = Intent(this, AddServicio::class.java)
            intent.putExtra("id_cliente", id_cliente)
            startActivity(intent)
        }

        getServicio()



    }

    fun getServicio() {
        val lista = lista_servicios
        lista.layoutManager = LinearLayoutManager(this)
        val adapter = AdaptadorServicios(llenarLista)
        adapter.notifyDataSetChanged()
        CoroutineScope(Dispatchers.IO).launch{
            AsyncTask.execute{
                val queue = Volley.newRequestQueue(this@DataCliente)
                val url = getString(R.string.urlAPI) + "/services/cliente/" + id_cliente
                val stringRequest = JsonArrayRequest(url,
                    Response.Listener { response ->
                        try {
                            for (i in 0 until response.length()) {
                                val id =
                                    response.getJSONObject(i).getInt("id")
                                val descripicion =
                                    response.getJSONObject(i).getString("servicio_desc")
                                var area =
                                    response.getJSONObject(i).getString("servicio_area").toString()
                                var dimension =
                                    response.getJSONObject(i).getString("servicio_dim").toString()
                                var frecuencia =
                                    response.getJSONObject(i).getString("servicio_frec").toString()
                                var precio =
                                    response.getJSONObject(i).getString("servicio_precio").toString()
                                var tipo =
                                    response.getJSONObject(i).getString("tipo").toString()
                                var cliente =
                                    response.getJSONObject(i).getInt("cliente")
                                llenarLista.add(ServicioX(id,descripicion, area, dimension, frecuencia, precio, tipo, cliente))
                            }
                            lista.adapter = adapter

                        } catch (e: JSONException) {
                            Toast.makeText(
                                this@DataCliente,
                                "Error al obtener los datos",
                                Toast.LENGTH_LONG
                            ).show()

                        }
                    }, Response.ErrorListener {
                        Toast.makeText(
                            this@DataCliente,
                            "Verifique que esté conectado a internet",
                            Toast.LENGTH_LONG
                        ).show()

                    })
                queue.add(stringRequest)
            }
        }
    }


    private fun showError(s:String){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }
}