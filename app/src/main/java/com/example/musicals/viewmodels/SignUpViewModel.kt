package com.example.musicals.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicals.models.repository.UserRepo
import com.example.musicals.models.roomdatabase.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SignUpViewModel(context: Context) : ViewModel() {

    private val userRepo = UserRepo(context)



    fun insertUser(user: User) : Boolean {
        var isSucces = false
           runBlocking {
               val job = viewModelScope.async(Dispatchers.IO) {
                   val boolean = userRepo.insertUser(user)
                   boolean
               }
               isSucces = job.await()
           }
        return isSucces
    }

    private fun getUserDetails(id: String): User? {
        return userRepo.getUserDetails(id)
    }

    fun checkUniqueUserId(id: String): Int {
        var result = 0
        runBlocking {
            val job = viewModelScope.async(Dispatchers.IO) {
                val user = getUserDetails(id)
                Log.i("Sign UP ", "${user?.user_Name} and ${user?.user_Password}")
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