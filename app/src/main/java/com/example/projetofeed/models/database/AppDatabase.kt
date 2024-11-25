package com.example.projetofeed.models.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.projetofeed.models.dao.UsuarioDao
import com.example.projetofeed.models.dao.PostDao
import com.example.projetofeed.models.entity.Usuario
import com.example.projetofeed.models.entity.Post

@Database(entities = [Usuario::class, Post::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun postDao(): PostDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
