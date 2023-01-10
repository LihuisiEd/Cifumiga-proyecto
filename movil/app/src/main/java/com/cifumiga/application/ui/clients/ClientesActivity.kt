package com.cifumiga.application.ui.clients

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.cifumiga.application.AdaptadorClientes
import com.cifumiga.application.AddClient
import com.cifumiga.application.R
import com.cifumiga.application.models.Cliente
import kotlinx.android.synthetic.main.activity_clientes.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException

class ClientesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clientes)

        val mensaje = mensaje_busqueda
        val swipe = swipeClientes

        btnAddClient.setOnClickListener() { val intent = Intent(this, AddClient::class.java)
            startActivity(intent) }

        val lista = lista_clientes
        lista.layoutManager = LinearLayoutManager(this)
        var llenarLista = ArrayList<Cliente>()
        val adapter = AdaptadorClientes(llenarLista)
        adapter.notifyDataSetChanged()

        txtClientSearch.addTextChangedListener{ clientFilter ->
            val llenarlistaClientes = llenarLista.filter {
                    cliente -> cliente.cliente_nombre.lowercase().contains(clientFilter.toString().lowercase()) }
            adapter.updateClientList(llenarlistaClientes as ArrayList<Cliente>)
            if (llenarlistaClientes.isEmpty()){
                mensaje.visibility = View.VISIBLE
            } else {
                mensaje.visibility = View.GONE
            }
        }

        CoroutineScope(Dispatchers.IO).launch{
            AsyncTask.execute{
                swipeconfig(swipe)
                val queue = Volley.newRequestQueue(this@ClientesActivity)
                val url = getString(R.string.urlAPI) + "/clientes/"
                val stringRequest = JsonArrayRequest(url,
                    Response.Listener { response ->
                        try {
                            for (i in 0 until response.length()) {
                                val id =
                                    response.getJSONObject(i).getInt("id")
                                val nombre =
                                    response.getJSONObject(i).getString("cliente_nombre")
                                var ruc =
                                    response.getJSONObject(i).getString("cliente_ruc").toString()
                                var contacto =
                                    response.getJSONObject(i).getString("cliente_contacto").toString()
                                var telefono =
                                    response.getJSONObject(i).getString("cliente_telefono").toString()
                                var correo =
                                    response.getJSONObject(i).getString("cliente_correo").toString()
                                if (ruc.equals("null")){
                                    ruc = "Sin ruc"
                                }
                                if (contacto.equals("null")){
                                    contacto = "Sin contacto"
                                }
                                if (correo.equals("null")){
                                    correo = "Sin correo"
                                }
                                if (telefono.equals("null")){
                                    telefono = "Sin telefono"
                                }
                                llenarLista.add(Cliente(id,nombre, ruc, contacto, telefono, correo))
                            }
                            lista.adapter = adapter
                            swipeEnd(swipe)
                        } catch (e: JSONException) {
                            Toast.makeText(
                                this@ClientesActivity,
                                "Error al obtener los datos",
                                Toast.LENGTH_LONG
                            ).show()
                            swipeEnd(swipe)
                        }
                    }, Response.ErrorListener {
                        Toast.makeText(
                            this@ClientesActivity,
                            "Verifique que esta conectado a internet",
                            Toast.LENGTH_LONG
                        ).show()
                        swipeEnd(swipe)
                    })
                queue.add(stringRequest)
            }
        }


    }

    private fun swipeconfig(swipe: SwipeRefreshLayout) {
        swipe.isEnabled = true
        swipe.isRefreshing = true
    }

    private fun swipeEnd(swipe: SwipeRefreshLayout) {
        swipe.isRefreshing = false
        swipe.isEnabled = false
    }

    private fun showError(s:String){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }
}