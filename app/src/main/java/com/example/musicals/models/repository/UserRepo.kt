package com.example.musicals.models.repository

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import com.example.musicals.models.roomdatabase.AppDatabase
import com.example.musicals.models.roomdatabase.User

class UserRepo(private val context: Context) {

    private val userDao = AppDatabase.getDatabase(context).getDao()
    suspend fun insertUser(user: User): Boolean {
        return try {
            userDao.insertUser(user)
            true
        } catch (e: SQLiteConstraintException) {
            false
        }
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    fun readLoginData(id: String, password: String): User? {
        return userDao.readLoginData(id, password)
    }

    fun getUserDetails(id: String): User? {
        return userDao.getUserDetails(id)
    }

}