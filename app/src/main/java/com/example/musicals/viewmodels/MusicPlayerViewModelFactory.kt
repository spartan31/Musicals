package com.example.musicals.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.musicals.databinding.ActivityMusicPlayerBinding
import com.example.musicals.views.notifcationservice.MusicService

class MusicPlayerViewModelFactory(
    val context: Context,
    val binding: ActivityMusicPlayerBinding
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MusicPlayerViewModel(context, binding  ) as T
    }
}