package com.cifumiga.application.ui.kilometraje

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.cifumiga.application.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_update_km.*

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class UpdateKM : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    var cal = Calendar.getInstance()
    var placa:String? = ""
    var empleado:String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_km)

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
                DatePickerDialog(this@UpdateKM,
                    dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }

        })

        val bundle :Bundle ?=intent.extras
        placa = bundle?.getString("placa")


        if (bundle!=null){
            this.title = getString(R.string.modificar_kilometraje)
            txtPlacaKM.isEnabled = false
            empleado = bundle.getString("empleado").toString()
            txtFecKM.setText(bundle.getString("fecha").toString())
            txtPlacaKM.setText(bundle.getString("placa").toString())
            txtInicioKM.setText(bundle.getString("inicio").toString())
            txtFinKM.setText(bundle.getString("fin").toString())
            txtTotalKM.setText(bundle.getString("total").toString())
        }

        bdUpdateKM.setOnClickListener(){
            val placa = txtPlacaKM.text.toString()
            val kilometraje = hashMapOf(
                "fecha" to txtFecKM.text.toString(),
                "placa" to placa,
                "inicial" to txtInicioKM.text.toString(),
                "final" to txtFinKM.text.toString(),
                "total" to txtTotalKM.text.toString(),
                "empleado" to empleado.toString()
            )
            db.collection("kilometrajes").document(placa)
                .set(kilometraje)
                .addOnSuccessListener { showError("Guardado con éxito") }
                .addOnFailureListener { showError("Problemas al guardar") }
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



    private fun updateDateInView() {
        val myFormat = "dd-MM-yyyy" // mention the format you need
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