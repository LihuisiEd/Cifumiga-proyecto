package com.cifumiga.application.ui.certificado

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.cifumiga.application.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_certificado.*
import java.text.SimpleDateFormat
import java.util.*

class CertificadoActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    var cal = Calendar.getInstance()
    var nombre:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_certificado)


        val bundle : Bundle? = intent.extras
        nombre = bundle?.getString("cliente").toString()


        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }

        val dateSetListenerFin = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInViewFin()
            }
        }

        btnFecCert.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(this@CertificadoActivity,
                    dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }

        })

        btnFinCert.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(this@CertificadoActivity,
                    dateSetListenerFin,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }
        })

        bdAddCertificado.setOnClickListener(){
            addCertificado()
        }


    }


    private fun addCertificado() {
        var fecha_inicio = txtFecCert.text.toString().trim()
        var fecha_fin = txtFinCert.text.toString().trim()
        if (fecha_inicio.isEmpty()){
            showError("Debe llenar la fecha del certificado")
            txtFecCert.error = "Campo requerido"
        } else{
            val certificado = hashMapOf(
                "fecha_inicio" to fecha_inicio,
                "fecha_final" to fecha_fin,
                "cliente" to nombre
            )
            db.collection("certificados").document(nombre.toString())
                .set(certificado)
                .addOnSuccessListener { showError("Guardado con éxito") }
                .addOnFailureListener { showError("Problemas al guardar") }
        }
    }

    private fun updateDateInView() {
        val myFormat = "dd-MM-yyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        txtFecCert.setText(sdf.format(cal.getTime()))
    }

    private fun updateDateInViewFin() {
        val myFormat = "dd-MM-yyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        txtFinCert.setText(sdf.format(cal.getTime()))
    }

    private fun showError(s:String){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }
}