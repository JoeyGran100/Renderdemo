package com.example.wingsdatingapp.local.storage.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.wingsdatingapp.model.UserDataModel
import com.example.wingsdatingapp.model.UserMatchEntity


@Database(entities = [UserDataModel::class,UserMatchEntity::class],version =4,exportSchema = true)
@TypeConverters(Converters::class)
abstract class UserInfoDatabase:RoomDatabase() {
    abstract fun userDataModelDao():UserDataModelDao
    abstract fun userMatch():UserMatchDao
}