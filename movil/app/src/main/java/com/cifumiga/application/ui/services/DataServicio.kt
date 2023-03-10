package com.cifumiga.application.ui.services

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.cifumiga.application.R
import com.cifumiga.application.ui.clients.ClientesActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_data_servicio.*

class DataServicio : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    var cliente: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_servicio)

        val bundle : Bundle? = intent.extras
        cliente = bundle?.getString("cliente").toString()


        btnDeleteService.setOnClickListener(){
            alertEliminar(getString(R.string.titulo_alerta_seguridad),
                getString(R.string.mensaje_eliminar_registro))
        }

    }

    private fun alertEliminar(t:String, s: String)  {
        val alertDialogBuilder = AlertDialog.Builder(this)
            .setTitle("Eliminar trámite")
            .setMessage(s)
            .setPositiveButton("Si", { dialog, whichButton ->
                db.collection("servicios").document(cliente).delete()
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