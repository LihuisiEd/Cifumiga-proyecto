package com.cifumiga.application.ui.clients

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isEmpty
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.cifumiga.application.AddClient
import com.cifumiga.application.*
import com.cifumiga.application.R
import com.cifumiga.application.databinding.FragmentClientsBinding
import com.cifumiga.application.models.Cliente
import com.android.volley.Response
import com.cifumiga.application.models.Servicio
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException


class ClientsFragment : Fragment() {

    private var _binding: FragmentClientsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(R.layout.fragment_clients, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mensaje =  view.findViewById<LinearLayout>(R.id.mensaje_busqueda)

        val swipe = view.findViewById<SwipeRefreshLayout>(R.id.swipeClientes)
        swipe.setColorSchemeResources(R.color.blue_500)

        val addClient = view.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.btnAddClient)
        addClient.setOnClickListener() { val intent = Intent(this@ClientsFragment.requireContext(), AddClient::class.java)
            startActivity(intent) }


        val lista = view.findViewById<RecyclerView>(R.id.lista_clientes)
        lista.layoutManager = LinearLayoutManager(activity)
        var llenarLista = ArrayList<Cliente>()
        val adapter = AdaptadorClientes(llenarLista)

        adapter.notifyDataSetChanged()

        var buscadorClient = view.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.txtClientSearch)
        buscadorClient.addTextChangedListener{ clientFilter ->
            val llenarlistaClientes = llenarLista.filter {
                    cliente -> cliente.cliente_nombre.lowercase().contains(clientFilter.toString().lowercase()) }
            adapter.updateClientList(llenarlistaClientes as ArrayList<Cliente>)
            if (llenarlistaClientes.isEmpty()){
                mensaje.visibility = View.VISIBLE
            } else {
                mensaje.visibility = View.GONE
            }
        }


        if (buscadorClient.isTextInputLayoutFocusedRectEnabled){
            CoroutineScope(Dispatchers.IO).launch{
                AsyncTask.execute{
                    swipeconfig(swipe)
                    val queue = Volley.newRequestQueue(activity)
                    val url = getString(R.string.urlAPI) + "/clientes/"
                    val stringRequest = JsonArrayRequest(url,
                        Response.Listener { response ->
                            try {
                                for (i in 0 until response.length()) {
                                    val id =
                                        response.getJSONObject(i).getInt("id")
                                    val nombre =
                                        response.getJSONObject(i).getString("cliente_nombre")
                                    var ruc =
                                        response.getJSONObject(i).getString("cliente_ruc").toString()
                                    if (ruc.equals("null")){
                                       ruc = "Sin ruc"
                                    }
                                    llenarLista.add(Cliente(id,nombre, ruc))
                                }
                                lista.adapter = adapter
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
