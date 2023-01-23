package com.cifumiga.application.ui.clients

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.cifumiga.application.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_client.*
import org.json.JSONException
import org.json.JSONObject

class AddClient : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_client)

        val contacto = txtAddContacto.text.toString().trim()
        val telefono = txtAddTelefono.text.toString().trim()
        val correo = txtAddCorreo.text.toString().trim()

        val bundle :Bundle ?=intent.extras

        if(bundle!=null){
            this.setTitle("Editar Cliente");
            txtAddRuc.setText(bundle.getString("ruc").toString())
            txtAddName.isEnabled = false
            txtAddName.setText(bundle.getString("nombre").toString())
            txtAddContacto.setText(bundle.getString("contacto").toString())
            txtAddTelefono.setText(bundle.getString("telefono").toString())
            txtAddCorreo.setText(bundle.getString("correo").toString())

        } else{
            this.setTitle("Agregar Cliente");
        }


        bdAddClient.setOnClickListener(){
            val nombre = txtAddName.text.toString()
            val cliente = hashMapOf(
                "nombre" to txtAddName.text.toString(),
                "ruc" to txtAddRuc.text.toString(),
                "contacto" to txtAddContacto.text.toString(),
                "telefono" to txtAddTelefono.text.toString(),
                "correo" to txtAddCorreo.text.toString()
            )
            db.collection("clientes").document(nombre)
                .set(cliente)
                .addOnSuccessListener { showError("Guardado con éxito") }
                .addOnFailureListener { showError("Problemas al guardar") }
        }



    }


    private fun showError(s:String){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }

}