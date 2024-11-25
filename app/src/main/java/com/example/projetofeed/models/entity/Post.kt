package com.example.projetofeed.models.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "post",)

data class Post(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val idUsuario : Int,
    val conteudo: String
)