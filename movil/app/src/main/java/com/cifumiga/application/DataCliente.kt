package com.cifumiga.application

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.cifumiga.application.models.Operacion
import kotlinx.android.synthetic.main.activity_data_cliente.*
import org.json.JSONException

class DataCliente : AppCompatActivity() {

    var id_cliente:Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_cliente)

        val bundle: Bundle?=intent.extras
        id_cliente = bundle?.getInt("id")
        nombre_cliente.setText(bundle?.getString("nombre"))
        ruc_cliente.setText(bundle?.getString("ruc"))

        getOperaciones()

        getServicio()

    }

    private fun getServicio() {

    }

    private fun getOperaciones() {
        val lista = lista_operaciones
        lista.layoutManager = LinearLayoutManager(this)
        var llenarLista = ArrayList<Operacion>()
        val adapter = AdaptadorOperacion(llenarLista)
        adapter.notifyDataSetChanged()

        AsyncTask.execute{

            val queue = Volley.newRequestQueue(this@DataCliente)
            val url = getString(R.string.urlAPI) + "/operaciones/cliente/" + id_cliente.toString()
            val stringRequest = JsonArrayRequest(url,
                Response.Listener { response ->
                    try {
                        for (i in 0 until response.length()) {
                            val id =
                                response.getJSONObject(i).getInt("id")
                            val contacto =
                                response.getJSONObject(i).getString("operacion_contacto")
                            var correo =
                                response.getJSONObject(i).getString("operacion_correo ")
                            var cliente =
                                response.getJSONObject(i).getInt("cliente")
                            llenarLista.add(Operacion(id,contacto, correo,cliente))
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
                        "Verifique que esta conectado a internet",
                        Toast.LENGTH_LONG
                    ).show()

                })
            queue.add(stringRequest)
        }
    }


    private fun showError(s:String){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }
}