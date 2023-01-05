package com.cifumiga.application

import android.content.Context
import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import com.cifumiga.application.ui.calendar.CalendarActivity
import com.cifumiga.application.ui.clients.ClientesActivity
import com.cifumiga.application.ui.kilometraje.KilometrajeActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val datos = this.getSharedPreferences("DatosUsuario", Context.MODE_PRIVATE)
        val userId = datos.getString("id", "id de usuario").toString()
        val userName = datos.getString("user_name", "Nombre de usuario").toString()
        val split = userName.split(" ").toTypedArray()
        val onlyName = split[0]
        saludo_ingreso.text = "Hola, " + onlyName

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
            intent.putExtra("id", userId)
            startActivity(intent)
        }

    }

}