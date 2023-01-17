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
import com.auth0.android.jwt.JWT
import com.cifumiga.application.R
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_perfil.*
import org.json.JSONException
import org.json.JSONObject

class Login : AppCompatActivity() {

    var JWTtoken = ""
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread.sleep(1000)
        setTheme(R.style.Theme_MyApplication_NoActionBar)
        setContentView(R.layout.activity_login)

        setup()

    }

    private fun setup() {
        sign.setOnClickListener(){
            if (emailEditTXT.text.isNotEmpty() && passEditTXT.text.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(emailEditTXT.text.toString(),
                        passEditTXT.text.toString()).addOnCompleteListener{
                        if (it.isSuccessful){
                            db.collection("usuarios").document(emailEditTXT.text.toString()).set(
                                hashMapOf(
                                    "permiso" to "user")
                            )
                            showHome(it.result.user?.email ?: "", ProviderType.BASIC)
                        } else {
                            showError("No se pudo ingresar")
                        }
                    }
            } else{
                showError("Llena las credenciales por favor")
            }
        }

        login.setOnClickListener(){
            if (emailEditTXT.text.isNotEmpty() && passEditTXT.text.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(emailEditTXT.text.toString(),
                        passEditTXT.text.toString()).addOnCompleteListener{
                        if (it.isSuccessful){
                            showHome(it.result.user?.email ?: "", ProviderType.BASIC)
                        } else {
                            showError("No se pudo ingresar")
                        }
                    }
            } else{
                showError("Llena las credenciales por favor")
            }

        }

    }

    private fun showHome(email:String, provider: ProviderType){
        val intent = Intent(this, MainActivity::class.java)
        val datos = getSharedPreferences("DatosUsuario", MODE_PRIVATE)
        val editor = datos.edit()
        editor.putString("email",email)
        editor.putString("provider", provider.name)
        editor.apply()
        startActivity(intent)

    }


    private fun showError(s:String){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }

    private fun ingresarPrueba(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}