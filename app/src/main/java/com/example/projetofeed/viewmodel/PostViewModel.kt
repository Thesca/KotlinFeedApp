package com.example.projetofeed.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetofeed.models.dao.PostDao
import com.example.projetofeed.models.entity.Post
import kotlinx.coroutines.launch

class PostViewModel(private val postDao: PostDao) : ViewModel() {

    var listaPosts = mutableStateOf(listOf<Post>())
        private set

    init {
        carregarPosts()
    }

    private fun carregarPosts() {
        viewModelScope.launch {
            listaPosts.value = postDao.buscarTodos()
        }
    }

    fun salvarPost(conteudo: String, idUsuario : Int): String {
        if (conteudo.isBlank()) {
            return "No que você está pensando?"
        }

        val post = Post(conteudo = conteudo, idUsuario = idUsuario)

        viewModelScope.launch {
            postDao.inserir(post)
            carregarPosts()
        }

        listaPosts.value += post

        return "Post feito com sucesso!"
    }
}