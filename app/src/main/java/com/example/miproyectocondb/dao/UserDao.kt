package com.example.miproyectocondb.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.miproyectocondb.database.entities.User

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Int): User?
}
