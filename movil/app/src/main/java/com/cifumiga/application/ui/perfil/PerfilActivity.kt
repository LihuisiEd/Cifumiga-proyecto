package com.cifumiga.application.ui.perfil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.cifumiga.application.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_perfil.*

class PerfilActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        val bundle: Bundle ? = intent.extras
        val email:String? = bundle?.getString("email")
        val nombre:String? = bundle?.getString("nombre")
        val permiso:String? = bundle?.getString("permiso")
        val celular:String? = bundle?.getString("celular")

        txtTipoUserE.text = permiso
        if(nombre.isNullOrBlank()){
            nameedittxt.setText("")
        }
        if(celular.isNullOrBlank()){
            celularedittxt.setText("")
        }else{
            nameedittxt.setText(nombre.toString())
            celularedittxt.setText(celular.toString())
        }


        saveButton.setOnClickListener(){
            val perfil = hashMapOf(
                "nombre" to nameedittxt.text.toString(),
                "permiso" to permiso,
                "celular" to celularedittxt.text.toString()
            )
            db.collection("empleados").document(email.toString())
                .set(perfil)
                .addOnSuccessListener { showError("Guardado con éxito") }
                .addOnFailureListener { showError("Problemas al guardar") }

        }
    }

    private fun showError(s:String){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }
}