package com.example.projetofeed.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projetofeed.models.dao.PostDao

class PostViewModelFactory(
    private val postDao: PostDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostViewModel::class.java)) {
            return PostViewModel(postDao) as T
        }
        throw IllegalArgumentException("Classe ViewModel desconhecida")
    }
}
