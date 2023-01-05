package com.cifumiga.application.ui.calendar

import android.app.DatePickerDialog
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.cifumiga.application.AdaptadorTramite
import com.cifumiga.application.AddTramite
import com.cifumiga.application.R
import com.cifumiga.application.models.Tramite
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import java.text.SimpleDateFormat
import java.util.*

class CalendarActivity : AppCompatActivity() {

    var cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        val mensaje = mensaje_fecha
        val swipe = swipeTramites

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }
        val addfecha = findViewById<Button>(R.id.btnSelectFec)
        addfecha.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(this@CalendarActivity,
                    dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }

        })

        btnAddTramite.setOnClickListener(){
            val intent = Intent (this@CalendarActivity, AddTramite::class.java)
            startActivity(intent)
        }

        val lista = lista_tramites
        lista.layoutManager = LinearLayoutManager(this)
        var llenarLista = ArrayList<Tramite>()
        val adapter = AdaptadorTramite(llenarLista)

        FiltroxFecha.addTextChangedListener{ fechaFilter ->
            val llenarlistaFechas = llenarLista.filter {
                    fecha -> fecha.tramite_fecha.lowercase().contains(fechaFilter.toString().lowercase()) }
            adapter.updateClientList(llenarlistaFechas as ArrayList<Tramite>)
            if (llenarlistaFechas.isEmpty()){
                mensaje.visibility = View.VISIBLE
            } else {
                mensaje.visibility = View.GONE
            }
        }

        CoroutineScope(Dispatchers.IO).launch{
            AsyncTask.execute{
                swipeconfig(swipe)
                val queue = Volley.newRequestQueue(this@CalendarActivity)
                val url = getString(R.string.urlAPI) + "/tramites/"
                val stringRequest = JsonArrayRequest(url,
                    Response.Listener { response ->
                        try {
                            for (i in 0 until response.length()) {
                                val id =
                                    response.getJSONObject(i).getInt("id")
                                val fecha =
                                    response.getJSONObject(i).getString("tramite_fecha")
                                val direccion =
                                    response.getJSONObject(i).getString("direccion")
                                val referencia =
                                    response.getJSONObject(i).getString("referencia")
                                var contacto =
                                    response.getJSONObject(i).getString("tramite_contacto").toString()
                                var telefono =
                                    response.getJSONObject(i).getString("tramite_telefono").toString()
                                var tipo_n1 =
                                    response.getJSONObject(i).getBoolean("tramite_nivel_1")
                                var tipo_n2 =
                                    response.getJSONObject(i).getBoolean("tramite_nivel_2")
                                var tipo_n3 =
                                    response.getJSONObject(i).getBoolean("tramite_nivel_3")
                                var tipo_n4 =
                                    response.getJSONObject(i).getBoolean("tramite_nivel_4")
                                var problemas =
                                    response.getJSONObject(i).getString("problemas")
                                var condicion_subestandar =
                                    response.getJSONObject(i).getString("condicion_subestandar")
                                var tipo =
                                    response.getJSONObject(i).getString("tipo").toString()
                                var cliente =
                                    response.getJSONObject(i).getString("cliente").toString()
                                llenarLista.add(Tramite(id,cliente, tipo,direccion,referencia,contacto,telefono,fecha,tipo_n1,tipo_n2,tipo_n3,tipo_n4,problemas,condicion_subestandar))
                            }
                            lista?.adapter = adapter
                            swipeEnd(swipe)
                        } catch (e: JSONException) {
                            Toast.makeText(
                                this@CalendarActivity,
                                "Error al obtener los datos",
                                Toast.LENGTH_LONG
                            ).show()
                            swipeEnd(swipe)
                        }
                    }, Response.ErrorListener {
                        Toast.makeText(
                            this@CalendarActivity,
                            "Verifique que esta conectado a internet",
                            Toast.LENGTH_LONG
                        ).show()
                        swipeEnd(swipe)
                    })
                queue.add(stringRequest)
            }
        }

    }

    private fun updateDateInView() {
        val myFormat = "yyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        txtFecLabel.visibility = View.VISIBLE
        txtFecSelected.setText(sdf.format(cal.getTime()))
        FiltroxFecha.setText(sdf.format(cal.getTime()))
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