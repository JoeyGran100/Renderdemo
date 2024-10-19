package com.example.wingsdatingapp.local.storage.room.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.wingsdatingapp.model.UserMatchEntity

@Dao
interface UserMatchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: UserMatchEntity)

    @Delete
    suspend fun deleteUser(user: UserMatchEntity)

    @Query("DELETE FROM MatchUserTable WHERE email = :userEmail")
    suspend fun deleteUserByEmail(userEmail: String)

    @Query("SELECT * FROM MatchUserTable")
    fun getAllUsers(): List<UserMatchEntity>
}