package com.cifumiga.application

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.widget.Toast
import com.auth0.android.jwt.JWT
import com.cifumiga.application.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_perfil.*
import org.json.JSONException
import org.json.JSONObject

class Login : AppCompatActivity() {

    private val Google_Sign_in = 100
    var JWTtoken = ""
    private val db = FirebaseFirestore.getInstance()
    var email : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread.sleep(1000)
        setTheme(R.style.Theme_MyApplication_NoActionBar)
        setContentView(R.layout.activity_login)

        setup()
        session()

    }

    override fun onStart() {
        super.onStart()

        layout_login.visibility = View.VISIBLE
    }


    private fun session() {

        val datos = this.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email:String = datos.getString("email", null).toString()

        if (email.contains("@")){
            layout_login.visibility = View.GONE
            showHome(email)
        }


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
                            showHome(it.result.user?.email ?: "")
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
                            showHome(it.result.user?.email ?: "")
                        } else {
                            showError("No se pudo ingresar")
                        }
                    }
            } else{
                showError("Llena las credenciales por favor")
            }
        }

        googleButton.setOnClickListener(){
            val googleConf =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
            val googleClient = GoogleSignIn.getClient(this, googleConf)
            googleClient.signOut()

            startActivityForResult(googleClient.signInIntent, Google_Sign_in)

        }

    }

    private fun showHome(email:String){
        /*
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("email",email)
        intent.putExtra("provider", provider.name)
        startActivity(intent)
         */
        val intent = Intent(this, MainActivity::class.java)
        val datos = getSharedPreferences(getString(R.string.prefs_file), MODE_PRIVATE).edit()
        datos.putString("email",email)
        datos.apply()
        startActivity(intent)
    }


    private fun showError(s:String){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Google_Sign_in){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if(account != null){
                    val credential = GoogleAuthProvider.getCredential(account.idToken,null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful){
                            showHome(account.email ?: "")
                        }else {
                            showError("No se logró")
                        }
                    }
                }
            } catch (e:ApiException){
                showError("Error Fatal")
            }

        }
    }
}