package com.cifumiga.application.models

import android.graphics.Movie
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url


interface Servicio{
    @GET("clientes")
    fun listClients(): Response<ArrayList<Cliente>>


}