package com.example.projetofeed.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetofeed.models.dao.UsuarioDao
import com.example.projetofeed.models.entity.Usuario
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class UsuarioViewModel(private val usuarioDao: UsuarioDao) : ViewModel() {

    var listaUsuarios = mutableStateOf(listOf<Usuario>())
        private set

    init {
        carregarUsuarios()
    }

    private fun carregarUsuarios() {
        viewModelScope.launch {
            listaUsuarios.value = usuarioDao.buscarTodos()
        }
    }

    fun buscarUsuarioEmail(email: String): Boolean {
        if (email.isBlank()) {
            return false
        }

        var flag = 0
        runBlocking {
            val usuario = usuarioDao.buscarUsuarioEmail(email)
            android.util.Log.wtf("UserRepository", "Usuario foi buscado corretamente no banco: $usuario")
            if (usuario != null && usuario.email == email) {
                flag = 1
            }
        }

        android.util.Log.wtf("UserRepository", "Flag caso o email tenha sido achado: $flag")
        return flag == 1
    }


    fun buscarUsuarioId(email: String): Int {
        if (email.isBlank()) {
            return 0
        }
        var usuario: Usuario? = null
        viewModelScope.launch {
            usuario = usuarioDao.buscarUsuarioId(email)
        }
        if (usuario == null) {
            return 0
        }

        return usuario!!.id
    }

    fun salvarUsuario(nome: String, email: String, senha: String): Boolean {
        if (nome.isBlank() || email.isBlank() || senha.isBlank()) {
            return false
        }

        val usuario = Usuario(nome = nome, email = email, senha = senha)

        val alreadyExist = buscarUsuarioEmail(email)
        android.util.Log.wtf("UserRepository", "Already exists valor: $alreadyExist")
        if(alreadyExist) {
            return false
        }
        viewModelScope.launch {
            usuarioDao.inserir(usuario)
            carregarUsuarios()
        }
        return true
    }

    fun validarLogin(email: String, senha: String): Boolean {
        val usuario = listaUsuarios.value.find { it.email == email } ?: return false

        if (usuario.senha == senha) {
            return true
        } else {
            return false
        }
    }
}
