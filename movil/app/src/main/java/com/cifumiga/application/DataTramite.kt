package com.cifumiga.application

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_data_tramite.*

class DataTramite : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_tramite)

        val policy =
            StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val bundle :Bundle ?=intent.extras

        val id_tramite = bundle?.getString("id")
        fec_Tramite.text = bundle?.getString("fecha")
        cliente_tramite.text = bundle?.getString("cliente")
        contact_Tramite.text = bundle?.getString("contacto")
        telefono_Tramite.text = bundle?.getString("telefono")
        direccion_Tramite.text = bundle?.getString("direccion")
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
            alertEliminar("¿Seguro quieres eliminar este trámite?", id_tramite.toString() )
        }

    }

    private fun alertEliminar(s: String, id_tramite:String) {
        val alertDialogBuilder = AlertDialog.Builder(this)
            .setTitle("Eliminar trámite")
            .setMessage(s)
            .setPositiveButton("Si", { dialog, whichButton ->
                eliminarTramite(id_tramite)
            })
            .setNegativeButton("No", { dialog, whichButton ->
                dialog.dismiss()
            })
            .show()
    }

    fun eliminarTramite(id:String) {
        AsyncTask.execute {
            val queue = Volley.newRequestQueue(this)
            var url = getString(R.string.urlAPI) + "/tramites/" + id
            val postRequest: StringRequest = object : StringRequest(
                Request.Method.DELETE, url,
                Response.Listener { response -> // response

                },
                Response.ErrorListener { response ->// error

                }
            ){}
            queue.add(postRequest)
        }
        val regresar = Intent(this, MainActivity::class.java)
        startActivity(regresar)
    }


}