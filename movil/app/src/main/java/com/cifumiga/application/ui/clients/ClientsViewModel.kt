package com.cifumiga.application.ui.clients

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ClientsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Clientes"
    }
    val text: LiveData<String> = _text
}