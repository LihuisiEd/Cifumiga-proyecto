package com.cifumiga.application

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_add_km.*
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class AddKm : AppCompatActivity() {

    var cal = Calendar.getInstance()
    var id_empleado:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_km)

        val bundle :Bundle?=intent.extras
        id_empleado = bundle?.getString("id")


        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }

        btnFecKM.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(this@AddKm,
                    dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }

        })


        bdSaveKM.setOnClickListener(){
            subirKilometraje()
        }

        bdCalcularKM.setOnClickListener(){
            calcularKM()
        }

    }

    private fun calcularKM() {
        val inicio = txtInicioKM.text.toString().trim()
        val fin = txtFinKM.text.toString().trim()
        if (fin.isNotEmpty() || inicio.isNotEmpty()){
            val ope = inicio.toDouble() - fin.toDouble()
            val opeOff = (ope * 100.0).roundToInt() /100.0
            txtTotalKM.setText(opeOff.toString())
        }
    }

    private fun subirKilometraje() {
        val fecha = txtFecKM.text.toString().trim()
        val inicio = txtInicioKM.text.toString().trim()
        val fin = txtFinKM.text.toString().trim()
        val total = txtTotalKM.text.toString().trim()
        val placa = txtPlacaKM.text.toString().trim()
        if (fecha.isEmpty() || inicio.isEmpty() || fin.isEmpty() || placa.isEmpty()){
            alertError("Asegúrate de llenar todos los campos")
        }
        if (fecha.isEmpty()){
            txtFecKM.error = "Fecha requerida"
        }
        if (inicio.isEmpty()){
            txtInicioKM.error = "Campo requerido"
        }
        if (fin.isEmpty()){
            txtFinKM.error = "Campo requerido"
        }

        if (placa.isEmpty()){
            txtPlacaKM.error = "Placa requerida"
        } else {
            val queue = Volley.newRequestQueue(this)
            val url = getString(R.string.urlAPI) + "/kilometrajes/"
            val jsonObj = JSONObject()

            jsonObj.put("fecha", fecha )
            jsonObj.put("placa", placa)
            jsonObj.put("kilometraje_inicio", inicio)
            jsonObj.put("kilometraje_fin", fin)
            jsonObj.put("kilometraje_total", total)
            jsonObj.put("empleado", id_empleado)

            val stringRequest = JsonObjectRequest(
                Request.Method.POST, url,jsonObj,
                Response.Listener { response ->
                    try {
                        showError("Kilometraje agregado con éxito")
                    } catch (e: JSONException){
                        showError("Datos incorrectos")
                    }
                }, Response.ErrorListener {
                    showError("Problemas de conexión, revisa tu conexión a internet")
                })
            queue.add(stringRequest)

        }
    }

    private fun updateDateInView() {
        val myFormat = "yyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        txtFecKM.setText(sdf.format(cal.getTime()))
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

    private fun showError(s:String){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }

}