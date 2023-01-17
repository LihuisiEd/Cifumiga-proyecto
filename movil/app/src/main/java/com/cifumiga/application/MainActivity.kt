package com.cifumiga.application

import android.content.Context
import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import com.cifumiga.application.ui.calendar.CalendarActivity
import com.cifumiga.application.ui.clients.ClientesActivity
import com.cifumiga.application.ui.kilometraje.KilometrajeActivity
import com.cifumiga.application.ui.perfil.PerfilActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

enum class ProviderType{
    BASIC
}

class MainActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    var nombre:String? = ""
    var permiso:String? = ""
    var celular:String?= ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val datos = this.getSharedPreferences("DatosUsuario", Context.MODE_PRIVATE)
        val email:String? = datos.getString("email", "email de usuario").toString()
        val provider:String? = datos.getString("provider", "provider de usuario").toString()

        imageButton1.isEnabled = false
        imageButton2.isEnabled = false
        imageButton3.isEnabled = false
        imageButton4.isEnabled = false

        setup(email ?: "", provider ?: "")

        if (!email.isNullOrEmpty()){
            db.collection("empleados").document(email.toString()).get()
                .addOnSuccessListener {
                    nombre = it.get("nombre") as String?
                    permiso = it.get("permiso") as String?
                    celular = it.get("celular") as String?
                    if (!nombre.isNullOrBlank()){
                        val split = nombre?.split(" ")?.toTypedArray()
                        val onlyName = split?.get(0)
                        saludo_ingreso.text = "Hola " + onlyName
                    }




                    if (permiso == "admin"){
                        imageButton1.isEnabled = true
                        imageButton2.isEnabled = true
                        imageButton3.isEnabled = true
                        imageButton4.isEnabled = true
                    }
                    if (permiso == "driver"){
                        imageButton3.isEnabled = true
                        imageButton4.isEnabled = true
                    }

                }

        }



        imageButton1.setOnClickListener() {
            val intent = Intent(this, ClientesActivity::class.java)
            startActivity(intent)
        }
        imageButton2.setOnClickListener() {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }


        imageButton3.setOnClickListener() {
            val intent = Intent(this, KilometrajeActivity::class.java)
            intent.putExtra("email", email)
            startActivity(intent)
        }



        imageButton4.setOnClickListener() {
            val intent = Intent(this, PerfilActivity::class.java)
            intent.putExtra("email", email)
            intent.putExtra("nombre", nombre)
            intent.putExtra("permiso", permiso)
            intent.putExtra("celular", celular)
            startActivity(intent)
        }



    }

    private fun setup(email:String, provider:String) {
        logout.setOnClickListener(){
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }
    }

}