package com.example.musicals.models.roomdatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("user_table")
data class User(
    @PrimaryKey
    val user_Id: String,
    val user_Name: String,
    val user_Password: String,
    val user_Image: String
)