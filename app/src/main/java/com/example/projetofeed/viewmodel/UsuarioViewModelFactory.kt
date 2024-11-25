package com.example.projetofeed.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projetofeed.models.dao.UsuarioDao

class UsuarioViewModelFactory(
    private val usuarioDao: UsuarioDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UsuarioViewModel::class.java)) {
            return UsuarioViewModel(usuarioDao) as T
        }
        throw IllegalArgumentException("Classe ViewModel desconhecida")
    }
}
