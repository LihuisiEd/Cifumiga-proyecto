package com.cifumiga.application.ui.calendar

import android.app.DatePickerDialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.cifumiga.application.*
import com.cifumiga.application.databinding.FragmentCalendarBinding
import com.cifumiga.application.models.Tramite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalendarFragment : Fragment() {

    var cal = Calendar.getInstance()
    private var _binding: FragmentCalendarBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(SlideshowViewModel::class.java)

        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mensaje =  view.findViewById<LinearLayout>(R.id.mensaje_fecha)

        val addfecha = view.findViewById<Button>(R.id.btnSelectFec)
        val addTram = view.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(
            R.id.btnAddTramite)

        val swipe = view.findViewById<SwipeRefreshLayout>(R.id.swipeTramites)
        swipe.setColorSchemeResources(R.color.blue_200)

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }

        addfecha.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                getActivity()?.let {
                    DatePickerDialog(
                        it,
                        dateSetListener,
                        // set DatePickerDialog to point to today's date when it loads up
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)).show()
                }
            }
        })

        addTram.setOnClickListener(){
            val intent = Intent (this@CalendarFragment.requireContext(), AddTramite::class.java)
            startActivity(intent)
        }

        val lista = view?.findViewById<RecyclerView>(R.id.lista_tramites)
        lista?.layoutManager = LinearLayoutManager(activity)
        var llenarLista = ArrayList<Tramite>()
        val adapter = AdaptadorTramite(llenarLista)

        val fechafiltro = view.findViewById<EditText>(R.id.FiltroxFecha)
        fechafiltro.addTextChangedListener{ fechaFilter ->
            val llenarlistaFechas = llenarLista.filter {
                    fecha -> fecha.tramite_fecha.lowercase().contains(fechaFilter.toString().lowercase()) }
            adapter.updateClientList(llenarlistaFechas as ArrayList<Tramite>)
            if (llenarlistaFechas.isEmpty()){
                mensaje.visibility = View.VISIBLE
            } else {
                mensaje.visibility = View.GONE
            }
        }


        CoroutineScope(Dispatchers.IO).launch{
            AsyncTask.execute{
                swipeconfig(swipe)
                val queue = Volley.newRequestQueue(activity)
                val url = getString(R.string.urlAPI) + "/tramites/"
                val stringRequest = JsonArrayRequest(url,
                    Response.Listener { response ->
                        try {
                            for (i in 0 until response.length()) {
                                val id =
                                    response.getJSONObject(i).getInt("id")
                                val fecha =
                                    response.getJSONObject(i).getString("tramite_fecha")
                                var contacto =
                                    response.getJSONObject(i).getString("tramite_contacto").toString()
                                var descripcion =
                                    response.getJSONObject(i).getString("tramite_descripcion").toString()
                                var tipo_n1 =
                                    response.getJSONObject(i).getBoolean("tramite_nivel_1")
                                var tipo_n2 =
                                    response.getJSONObject(i).getBoolean("tramite_nivel_2")
                                var tipo_n3 =
                                    response.getJSONObject(i).getBoolean("tramite_nivel_3")
                                var tipo_n4 =
                                    response.getJSONObject(i).getBoolean("tramite_nivel_4")
                                var tipo =
                                    response.getJSONObject(i).getString("tipo").toString()
                                var cliente =
                                    response.getJSONObject(i).getString("cliente").toString()
                                llenarLista.add(Tramite(id,cliente, tipo,contacto,descripcion,fecha,tipo_n1,tipo_n2,tipo_n3,tipo_n4))
                            }
                            lista?.adapter = adapter
                            swipeEnd(swipe)
                        } catch (e: JSONException) {
                            Toast.makeText(
                                activity,
                                "Error al obtener los datos",
                                Toast.LENGTH_LONG
                            ).show()
                            swipeEnd(swipe)
                        }
                    }, Response.ErrorListener {
                        Toast.makeText(
                            activity,
                            "Verifique que esta conectado a internet",
                            Toast.LENGTH_LONG
                        ).show()
                        swipeEnd(swipe)
                    })
                queue.add(stringRequest)
            }
        }

    }

    private fun updateDateInView() {
        val fechaLabel = view?.findViewById<TextView>(R.id.txtFecLabel)
        val fechaSeleccionada = view?.findViewById<TextView>(R.id.txtFecSelected)
        val myFormat = "yyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        fechaLabel?.visibility = View.VISIBLE
        fechaSeleccionada?.setText(sdf.format(cal.getTime()))
        var fechafiltro = view?.findViewById<EditText>(R.id.FiltroxFecha)
        fechafiltro?.setText(sdf.format(cal.getTime()))
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
        Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}