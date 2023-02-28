package com.example.musicals.viewmodels

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicals.models.repository.UserRepo
import com.example.musicals.models.roomdatabase.User
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ProfileViewModel(val context: Context) : ViewModel() {
    private val userRepo = UserRepo(context)

    var userId = MutableLiveData<String>()
    var userName = MutableLiveData<String>()
    var userPassword = MutableLiveData<String>()
    var userProfilePic = MutableLiveData<Uri>()


    fun readLoginData(id: String): User {
        var userInfo = User("demo", "demo", "Demo", "Demo")
        runBlocking {
            val result = viewModelScope.async(Dispatchers.IO) {
                userInfo = userRepo.getUserDetails(id)!!
            }
            result.await()
        }
        userId.value = userInfo.user_Id
        userName.value = userInfo.user_Name
        userPassword.value = userInfo.user_Password
        userProfilePic.value = Uri.parse(userInfo.user_Image)
        return userInfo
    }

    fun updateData() {
        val user = User(
            userId.value.toString(),
            userName.value.toString(),
            userPassword.value.toString(),
            userProfilePic.value.toString()
        )
        viewModelScope.launch(Dispatchers.IO) {
            userRepo.updateUser(user)
        }
    }

    fun setImagefromDatabase(circleImageView: CircleImageView) {
        circleImageView.setImageURI(userProfilePic.value)
    }
}