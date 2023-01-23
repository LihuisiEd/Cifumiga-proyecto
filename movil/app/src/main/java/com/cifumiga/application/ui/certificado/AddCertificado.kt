package com.cifumiga.application.ui.certificado

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.cifumiga.application.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_certificado.*

class AddCertificado : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    var nombre:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_certificado)

        val bundle : Bundle? = intent.extras
        nombre = bundle?.getString("cliente").toString()
        if (bundle != null){
            bdDelCetificado.visibility = View.VISIBLE
            txtFecCert.setText(bundle.getString("fecha_inicio").toString())
            txtFecCert.isEnabled = false
            txtFinCert.setText(bundle.getString("fecha_final").toString())
            txtFinCert.isEnabled = false

        }

        bdDelCetificado.setOnClickListener(){
            alertEliminar(getString(R.string.titulo_alerta_seguridad),
                getString(R.string.mensaje_eliminar_registro))
        }
    }

    private fun alertEliminar(t:String, s: String)  {
        val alertDialogBuilder = AlertDialog.Builder(this)
            .setTitle("Eliminar Certificado")
            .setMessage(s)
            .setPositiveButton("Si", { dialog, whichButton ->
                db.collection("certificados").document(nombre.toString()).delete()
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