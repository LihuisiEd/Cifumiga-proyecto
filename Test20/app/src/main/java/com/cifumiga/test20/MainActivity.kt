package com.cifumiga.test20

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.cifumiga.test20.models.Cliente

import org.json.JSONException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val swipe = findViewById<SwipeRefreshLayout>(R.id.swipeClientes)
        swipe.setColorSchemeResources(R.color.purple_200)


        val listaCli = findViewById<RecyclerView>(R.id.listClient)
        listaCli.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        listaCli.layoutManager = LinearLayoutManager(this)
        var llenarLista = ArrayList<Cliente>()

        runOnUiThread{
            swipeconfig(swipe)
            val queue = Volley.newRequestQueue(this)
            val url = "http://localhost:8000//clientes/"
            val stringRequest = JsonArrayRequest(url,
                Response.Listener { response ->
                    try {
                        for (i in 0 until response.length()) {
                            val nombre =
                                response.getJSONObject(i).getString("cliente_nombre")
                            val ruc =
                                response.getJSONObject(i).getString("cliente_ruc").toString()
                            llenarLista.add(Cliente(nombre, ruc)
                            )
                        }
                        val adapter = AdaptadorClientes(llenarLista)
                        listaCli.adapter = adapter

                        swipeEnd(swipe)


                    } catch (e: JSONException) {
                        showError("Error de conexion")
                    }
                }, Response.ErrorListener {
                    showError("No accede dentro")
                    swipeEnd(swipe)
                })
            queue.add(stringRequest)
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