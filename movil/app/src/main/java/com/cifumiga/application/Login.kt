package com.cifumiga.application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.cifumiga.application.R
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread.sleep(1000)
        setTheme(R.style.Theme_MyApplication_NoActionBar)
        setContentView(R.layout.activity_login)

        val policy =
            StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        login.setOnClickListener(){
            //loginValidate()
            ingresarPrueba()
        }

        go_register.setOnClickListener(){
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

    }

    private fun loginValidate() {
        val email = login_correo.text.toString()
        val pass = login_password.text.toString()
        if(email.isEmpty()){
            login_correo.error = "Correo requerido"
            login_correo.requestFocus()
        }
        if (pass.isEmpty()){
            login_password.error = "Contraseña requerida"
            login_password.requestFocus()
        }
        else{
            val queue = Volley.newRequestQueue(this)
            val url = getString(R.string.urlAPI) + "/login/"
            val jsonObj = JSONObject()
            jsonObj.put("email", email)
            jsonObj.put("password",pass)

            val stringRequest =  JsonObjectRequest(
                Request.Method.POST, url,jsonObj,
                Response.Listener { response ->
                    try{
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } catch (e: JSONException){
                        showError("Hey, estos datos no van")
                    }
                }, Response.ErrorListener {
                    showError("Revisa los datos ingresados, o revisa tu conexión a internet")
                })
            queue.add(stringRequest)
        }
    }

    private fun showError(s:String){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }

    private fun ingresarPrueba(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}