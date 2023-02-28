package com.example.musicals.models.roomdatabase

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(user: User)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM user_table WHERE user_Id LIKE :id AND user_Password LIKE :password")
    fun readLoginData(id :String , password : String) : User?

    @Query("select * from user_table where user_Id LIKE :id")
    fun getUserDetails(id: String ) : User?

}