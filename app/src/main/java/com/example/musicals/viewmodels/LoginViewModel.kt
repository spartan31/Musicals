package com.example.musicals.viewmodels

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicals.models.repository.UserRepo
import com.example.musicals.models.roomdatabase.User
import kotlinx.coroutines.*

class LoginViewModel( val context: Context) : ViewModel() {

    val userRepo = UserRepo(context)
    var userId = MutableLiveData<String>()
//    private val viewModelJob = Job()

    fun readLoginData(id: String, password: String): User? {
        return userRepo.readLoginData(id, password)
    }

    fun isValidLoginData(id: String, password: String): Int {
        var result: Int = 1
        runBlocking {
            var job = viewModelScope.async(Dispatchers.IO) {
                val user = readLoginData(id, password)
                val pref = context.getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
                val editor = pref.edit()
                editor.putString("currentUserId",user?.user_Id.toString())
                editor.apply()

                Log.i("Login Status ", "${user?.user_Name} and ${user?.user_Password}")
                if (user != null) {
                    -1
                } else {
                    100
                }
            }
            result = job.await()
        }
        return result
    }

}