package com.example.projetofeed.models.dao

import androidx.room.*
import com.example.projetofeed.models.entity.Post

@Dao
interface PostDao {
    @Insert
    suspend fun inserir(post: Post)

    @Query("SELECT * FROM post")
    suspend fun buscarTodos(): List<Post>

}
