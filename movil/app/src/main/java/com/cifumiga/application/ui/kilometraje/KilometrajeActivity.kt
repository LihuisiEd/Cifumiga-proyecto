package com.cifumiga.application.ui.kilometraje

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.cifumiga.application.AdaptadorKilometraje
import com.cifumiga.application.AddKm
import com.cifumiga.application.R
import com.cifumiga.application.models.Kilometraje
import com.cifumiga.application.ui.clients.ClientesActivity
import kotlinx.android.synthetic.main.activity_kilometraje.*
import org.json.JSONException

class KilometrajeActivity : AppCompatActivity() {

    var id_empleado:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kilometraje)

        val swipe = swipeKilometrajes

        val bundle :Bundle?=intent.extras
        id_empleado = bundle?.getString("id")

        btnAddKM.setOnClickListener(){
            val intent = Intent(this, AddKm::class.java)
            intent.putExtra("id", id_empleado)
            startActivity(intent)
        }

        val lista = lista_kilometrajes
        lista.layoutManager = LinearLayoutManager(this)
        var llenarLista = ArrayList<Kilometraje>()
        val adapter = AdaptadorKilometraje(llenarLista)
        adapter.notifyDataSetChanged()

        AsyncTask.execute{
            swipeconfig(swipe)
            val queue = Volley.newRequestQueue(this@KilometrajeActivity)
            val url = getString(R.string.urlAPI) + "/kilometrajes/empleado/" + id_empleado
            val stringRequest = JsonArrayRequest(url,
                Response.Listener { response ->
                    try {
                        for (i in 0 until response.length()) {
                            val id =
                                response.getJSONObject(i).getInt("id")
                            val empleado =
                                response.getJSONObject(i).getInt("empleado")
                            val fecha =
                                response.getJSONObject(i).getString("fecha")
                            val placa =
                                response.getJSONObject(i).getString("placa")
                            var fin =
                                response.getJSONObject(i).getString("kilometraje_fin").toString()
                            var inicio =
                                response.getJSONObject(i).getString("kilometraje_inicio").toString()
                            var total =
                                response.getJSONObject(i).getString("kilometraje_total").toString()
                            if (total.equals("null")){
                                total = "Aún sin calcular"
                            }
                            llenarLista.add(Kilometraje(id,empleado, fecha,placa,fin,inicio,total))
                        }
                        lista.adapter = adapter
                        swipeEnd(swipe)
                    } catch (e: JSONException) {
                        Toast.makeText(
                            this@KilometrajeActivity,
                            "Error al obtener los datos",
                            Toast.LENGTH_LONG
                        ).show()
                        swipeEnd(swipe)
                    }
                }, Response.ErrorListener {
                    Toast.makeText(
                        this@KilometrajeActivity,
                        "Verifique que esta conectado a internet",
                        Toast.LENGTH_LONG
                    ).show()
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