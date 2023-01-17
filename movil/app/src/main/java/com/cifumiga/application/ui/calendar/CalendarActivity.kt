package com.cifumiga.application.ui.calendar

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cifumiga.application.R
import com.cifumiga.application.models.Tramite
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import kotlinx.android.synthetic.main.activity_calendar.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalendarActivity : AppCompatActivity() {


    private lateinit var db : FirebaseFirestore
    private lateinit var tramiteRecyclerView: RecyclerView
    private lateinit var tramiteArrayList: ArrayList<Tramite>
    private lateinit var adapter : AdaptadorTramite
    var cal = Calendar.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        val mensaje = mensaje_fecha

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }
        val addfecha = findViewById<Button>(R.id.btnSelectFec)
        addfecha.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(this@CalendarActivity,
                    dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }

        })

        btnAddTramite.setOnClickListener(){
            val intent = Intent (this@CalendarActivity, AddTramite::class.java)
            startActivity(intent)
        }

        tramiteRecyclerView = findViewById<RecyclerView>(R.id.lista_tramites)
        tramiteRecyclerView.layoutManager = LinearLayoutManager(this)
        tramiteRecyclerView.setHasFixedSize(true)
        tramiteArrayList = ArrayList<Tramite>()
        adapter = AdaptadorTramite(tramiteArrayList)
        tramiteRecyclerView.adapter = adapter

        FiltroxFecha.addTextChangedListener{ fechaFilter ->
            val llenarlistaFechas = tramiteArrayList.filter {
                    fecha -> fecha.fecha?.lowercase().toString().contains(fechaFilter.toString().lowercase()) }
            adapter.updateClientList(llenarlistaFechas as ArrayList<Tramite>)
            if (llenarlistaFechas.isEmpty()){
                mensaje.visibility = View.VISIBLE
            } else {
                mensaje.visibility = View.GONE
            }
        }

        getTramitesData()

    }

    private fun getTramitesData() {
        db = FirebaseFirestore.getInstance()
        db.collection("tramites").orderBy("cliente", Query.Direction.ASCENDING).
        addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null){
                    showError("No se logró")
                    return
                }
                for (dc : DocumentChange in value?.documentChanges!!){
                    if (dc.type == DocumentChange.Type.ADDED){
                        tramiteArrayList.add(dc.document.toObject(Tramite::class.java))
                    }
                }
                adapter.notifyDataSetChanged()

            }
        })

    }

    private fun updateDateInView() {
        val myFormat = "dd-MM-yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        txtFecLabel.visibility = View.VISIBLE
        txtFecSelected.setText(sdf.format(cal.getTime()))
        FiltroxFecha.setText(sdf.format(cal.getTime()))
    }


    private fun swipeconfig(swipe: SwipeRefreshLayout) {
        swipe.isEnabled = true
        swipe.isRefreshing = true
    }

    private fun swipeEnd(swipe: SwipeRefreshLayout) {
        swipe.isRefreshing = false
        swipe.isEnabled = false
    }

    private fun showError(s:String){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }
}