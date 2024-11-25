package com.example.projetofeed.models.dao

import androidx.room.*
import com.example.projetofeed.models.entity.Usuario

@Dao
interface UsuarioDao {

    @Insert
    suspend fun inserir(usuario: Usuario)

    @Query("SELECT * FROM usuario")
    suspend fun buscarTodos(): List<Usuario>

    @Query("SELECT * FROM usuario WHERE email == :email LIMIT 1")
    suspend fun buscarUsuarioEmail(email : String): Usuario?

    @Query("SELECT * FROM usuario WHERE email == :email LIMIT 1")
    suspend fun buscarUsuarioId(email : String): Usuario?

    @Delete
    suspend fun deletar(usuario: Usuario)

    @Update
    suspend fun atualizar(usuario: Usuario)
}
