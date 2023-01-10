package com.cifumiga.application

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_data_kilometraje.*

class DataKilometraje : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_kilometraje)

        val bundle :Bundle ?=intent.extras
        val id_kilometraje = bundle?.getInt("id")
        fec_kilometraje.setText(bundle?.getString("fecha"))
        placa_kilometraje.setText(bundle?.getString("placa"))
        kminicial_kilometraje.setText(bundle?.getString("inicio"))
        kmfinal_kilometraje.setText(bundle?.getString("fin"))
        kmtotal_kilometraje.setText(bundle?.getString("total"))

        btnDeleteKM.setOnClickListener(){
            if (id_kilometraje != null) {
                alertEliminar("¿Seguro quieres eliminar este kilometraje?", id_kilometraje)
            }
        }

    }

    private fun alertEliminar(s: String, id_kilometraje:Int)  {
        val alertDialogBuilder = AlertDialog.Builder(this)
            .setTitle("Eliminar trámite")
            .setMessage(s)
            .setPositiveButton("Si", { dialog, whichButton ->
                eliminarKim(id_kilometraje)
            })
            .setNegativeButton("No", { dialog, whichButton ->
                dialog.dismiss()
            })
            .show()
    }

    private fun eliminarKim(id_kilometraje:Int) {
        AsyncTask.execute {
            val queue = Volley.newRequestQueue(this)
            var url = getString(R.string.urlAPI) + "/kilometrajes/" + id_kilometraje.toString()
            val postRequest: StringRequest = object : StringRequest(
                Request.Method.DELETE, url,
                Response.Listener { response -> // response
                    showError("Kilometraje eliminado")
                },
                Response.ErrorListener { response ->// error
                    showError("Ops! No pudimos eliminarlo")
                }
            ){}
            queue.add(postRequest)
        }
        val regresar = Intent(this, MainActivity::class.java)
        startActivity(regresar)
    }

    private fun showError(s:String){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }
}