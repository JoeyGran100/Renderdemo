package com.example.wingsdatingapp.local.storage.room.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.wingsdatingapp.model.UserDataModel

@Dao
interface UserDataModelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserDataModel)

    @Update
    suspend fun updateUser(user: UserDataModel)


    @Query("SELECT * FROM UserInfoDB WHERE id = :userId")
    suspend fun getUserById(userId: Int): UserDataModel?

    @Query("SELECT * FROM UserInfoDB LIMIT 1")
    suspend fun getLoggedInUser(): UserDataModel?

    @Query("DELETE FROM UserInfoDB")
    suspend fun deleteAllUsers()
}