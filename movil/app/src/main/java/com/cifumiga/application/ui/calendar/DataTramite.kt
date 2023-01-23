package com.cifumiga.application.ui.calendar

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.cifumiga.application.MainActivity
import com.cifumiga.application.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_data_tramite.*

class DataTramite : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    var id: String?=""
    var nombre:String? = ""
    var numero: String? = ""
    var fecha:String? =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_tramite)

        val policy =
            StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val bundle :Bundle ?=intent.extras

        nombre = bundle?.getString("cliente")?.uppercase()
        id = bundle?.getString("id")
        numero = bundle?.getString("id")
        id_Tramite.text = bundle?.getString("id")
        culminacion_Tramite.text = bundle?.getString("culminacion")
        fec_Tramite.text = bundle?.getString("fecha")
        fecha = bundle?.getString("fecha")
        cliente_tramite.text = bundle?.getString("cliente")
        contact_Tramite.text = bundle?.getString("contacto")
        telefono_Tramite.text = bundle?.getString("telefono")
        direccion_Tramite.text = bundle?.getString("direccion")
        frecuencia_Tramite.text = bundle?.getString("frecuencia")
        referencia_Tramite.text = bundle?.getString("referencia")
        typeServ_Tramite.text = bundle?.getString("tipo")
        problemas_Tramite.text = bundle?.getString("problemas")
        condiciones_Tramite.text = bundle?.getString("condiciones")
        val nivel_1 = bundle?.getString("nivel_1")
        val nivel_2 = bundle?.getString("nivel_2")
        val nivel_3 = bundle?.getString("nivel_3")
        val nivel_4 = bundle?.getString("nivel_4")

        if (nivel_1 == "true"){
            nivel1_tramite.visibility = View.VISIBLE
            nivel1_tramite.text = getString(R.string.nivel1)
        }
        if (nivel_2 == "true"){
            nivel2_tramite.visibility = View.VISIBLE
            nivel2_tramite.text = getString(R.string.nivel2)
        }
        if (nivel_3 == "true"){
            nivel3_tramite.visibility = View.VISIBLE
            nivel3_tramite.text = getString(R.string.nivel3)
        }
        if (nivel_4 == "true"){
            nivel4_tramite.visibility = View.VISIBLE
            nivel4_tramite.text = getString(R.string.nivel4)
        }
        if (nivel_1 == "false" && nivel_2 == "false" && nivel_3 == "false" && nivel_4 == "false"){
            sin_niveles.visibility = View.VISIBLE
        } else {
            sin_niveles.visibility = View.GONE
        }

        btnDeleteTram.setOnClickListener(){
            alertEliminar(getString(R.string.titulo_alerta_seguridad),
                getString(R.string.mensaje_eliminar_registro))
        }

    }

    private fun alertEliminar(t:String, s: String)  {
        val alertDialogBuilder = AlertDialog.Builder(this)
            .setTitle(t)
            .setMessage(s)
            .setPositiveButton("Si", { dialog, whichButton ->
                db.collection("hojas_trabajo").document("$id $fecha").delete()
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