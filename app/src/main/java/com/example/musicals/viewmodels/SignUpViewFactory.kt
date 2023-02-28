package com.example.musicals.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.musicals.views.fragments.SignUpFragment

class SignUpViewFactory(private val context: Context) :ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SignUpViewModel(context) as T
    }
}