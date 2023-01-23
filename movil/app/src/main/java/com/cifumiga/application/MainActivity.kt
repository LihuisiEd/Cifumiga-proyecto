package com.cifumiga.application

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.cifumiga.application.ui.calendar.CalendarActivity
import com.cifumiga.application.ui.clients.ClientesActivity
import com.cifumiga.application.ui.kilometraje.KilometrajeActivity
import com.cifumiga.application.ui.perfil.PerfilActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

enum class ProviderType{
    BASIC,
    GOOGLE
}

class MainActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    var nombre:String? = ""
    var permiso:String? = ""
    var celular:String?= ""

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        /*
        val bundle : Bundle?= intent.extras
        val email:String = bundle?.getString("email").toString()
        val provider:String = bundle?.getString("provider").toString()
        setup(email, provider)

         */

        val datos = this.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email:String = datos.getString("email", null).toString()
        val provider:String = datos.getString("provider", null).toString()
        setup(email ?: "", provider ?: "")

        email_ingreso.text = email


/*
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.putString("provider", provider)
        prefs.apply()


 */

        imageButton1.isEnabled = false
        imageButton2.isEnabled = false
        imageButton3.isEnabled = false
        imageButton4.isEnabled = false



        if (!email.isNullOrEmpty()){
            db.collection("empleados").document(email.toString()).get()
                .addOnSuccessListener {
                    nombre = it.get("nombre") as String?
                    permiso = it.get("permiso") as String?
                    celular = it.get("celular") as String?
                    if (!nombre.isNullOrBlank()){
                        val split = nombre?.split(" ")?.toTypedArray()
                        val onlyName = split?.get(0)
                        saludo_ingreso.text = "Hola $onlyName "
                        permiso_ingreso.text = "($permiso)"
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
                    if (permiso == "lector"){
                        imageButton2.isEnabled = true
                    }

                }

        }



        imageButton1.setOnClickListener() {
            val intent = Intent(this, ClientesActivity::class.java)
            intent.putExtra("permiso", permiso)
            startActivity(intent)
        }
        imageButton2.setOnClickListener() {
            val intent = Intent(this, CalendarActivity::class.java)
            intent.putExtra("permiso", permiso)
            startActivity(intent)
        }


        imageButton3.setOnClickListener() {
            val intent = Intent(this, KilometrajeActivity::class.java)
            intent.putExtra("email", email)
            intent.putExtra("permiso", permiso)
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

        link_pagina.setOnClickListener(){
            val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.cifumiga.com/"))
            startActivity(myIntent)
        }

        brochur.setOnClickListener(){
            val intent = Intent(this, PdfActivity::class.java)
            startActivity(intent)
        }


    }



    private fun setup(email:String, provider:String) {
        logout.setOnClickListener(){

            //Borrado de datos
            val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()

            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }
    }




}