package com.cifumiga.application.ui.kilometraje

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.cifumiga.application.MainActivity
import com.cifumiga.application.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_data_kilometraje.*

class DataKilometraje : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    var placa:String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_kilometraje)

        val bundle :Bundle ?=intent.extras
        placa = bundle?.getString("placa")
        val id_kilometraje = bundle?.getInt("id")
        fec_kilometraje.setText(bundle?.getString("fecha"))
        placa_kilometraje.setText(bundle?.getString("placa"))
        kminicial_kilometraje.setText(bundle?.getString("inicio"))
        kmfinal_kilometraje.setText(bundle?.getString("fin"))
        kmtotal_kilometraje.setText(bundle?.getString("total"))

        btnDeleteKM.setOnClickListener(){
            alertEliminar(getString(R.string.titulo_alerta_seguridad),
                getString(R.string.mensaje_eliminar_registro))
        }

    }

    private fun alertEliminar(t:String, s: String)  {
        val alertDialogBuilder = AlertDialog.Builder(this)
            .setTitle(t)
            .setMessage(s)
            .setPositiveButton("Si", { dialog, whichButton ->
                db.collection("kilometrajes").document(placa.toString()).delete()
                showError(getString(R.string.registro_eliminado))
            })
            .setNegativeButton("No", { dialog, whichButton ->
                dialog.dismiss()
            })
            .show()
    }

    private fun showError(s:String){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }

}