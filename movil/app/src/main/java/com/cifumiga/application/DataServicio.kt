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
import com.cifumiga.application.ui.clients.ClientesActivity
import kotlinx.android.synthetic.main.activity_data_servicio.*

class DataServicio : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_servicio)

        val bundle : Bundle? = intent.extras
        val id_servicio = bundle?.getInt("id")
        tipo_servicio.text = bundle?.getString("tipo")
        area_servicio.text = bundle?.getString("area")
        dimesion_servicio.text = bundle?.getString("dim")
        frecuencia_servicio.text = bundle?.getString("frec")
        precio_servicio.text = bundle?.getString("precio")
        desc_servicio.text = bundle?.getString("desc")


        btnDeleteService.setOnClickListener(){
            if (id_servicio != null) {
                alertEliminar("¿Seguro quieres eliminar este servicio?", id_servicio)
            }
        }

    }

    private fun alertEliminar(s: String, id_servicio:Int)  {
        val alertDialogBuilder = AlertDialog.Builder(this)
            .setTitle("Eliminar trámite")
            .setMessage(s)
            .setPositiveButton("Si", { dialog, whichButton ->
                eliminarKim(id_servicio)
            })
            .setNegativeButton("No", { dialog, whichButton ->
                dialog.dismiss()
            })
            .show()
    }

    private fun eliminarKim(id_servicio:Int) {
        AsyncTask.execute {
            val queue = Volley.newRequestQueue(this)
            var url = getString(R.string.urlAPI) + "/services/" + id_servicio.toString()
            val postRequest: StringRequest = object : StringRequest(
                Request.Method.DELETE, url,
                Response.Listener { response -> // response
                    showError("Servicio eliminado")
                },
                Response.ErrorListener { response ->// error
                    showError("Ops! No pudimos eliminarlo")
                }
            ){}
            queue.add(postRequest)
        }
        val regresar = Intent(this, ClientesActivity::class.java)
        startActivity(regresar)
    }
    private fun showError(s:String){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }
}