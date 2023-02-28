package com.example.musicals.views.activity


import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.musicals.R
import com.example.musicals.databinding.ActivityMusicPlayerBinding
import com.example.musicals.viewmodels.MusicPlayerViewModel
import com.example.musicals.viewmodels.MusicPlayerViewModelFactory
import com.example.musicals.models.roomdatabase.AudioModel
import com.example.musicals.views.notifcationservice.MusicService

class MusicPlayerActivity : AppCompatActivity(), View.OnClickListener, ServiceConnection {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var binding: ActivityMusicPlayerBinding
        lateinit var audioList: ArrayList<AudioModel>
        var songPosition: Int = -1
        var musicService: MusicService? = null

        @SuppressLint("StaticFieldLeak")
        lateinit var viewModel: MusicPlayerViewModel
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        audioList = intent.getSerializableExtra("songList") as ArrayList<AudioModel>
        songPosition = intent.getIntExtra("position", -1)


        viewModel = ViewModelProvider(this, MusicPlayerViewModelFactory(this, binding)).get(
            MusicPlayerViewModel::class.java
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        val serviceIntent = Intent(this, MusicService::class.java)
        bindService(serviceIntent, this, BIND_AUTO_CREATE)
        startService(serviceIntent)


        try {
            viewModel.updateLiveData(audioList, songPosition)
            clickListeners()

        } catch (e: Exception) {

        }

    }

    private fun clickListeners() {
        binding.playPauseButton.setOnClickListener(this)
        binding.previousSong.setOnClickListener(this)
        binding.nextSong.setOnClickListener(this)
    }


    override fun onClick(p0: View?) {
        when (p0) {
            binding.playPauseButton -> {
                if (MusicPlayerViewModel.songPlaying.value != 0) {
                    MusicPlayerViewModel.musicService!!.mediaPlayer!!.pause()
                    binding.playPauseButton.setImageResource(R.drawable.play_pause_button)
                    MusicPlayerViewModel.curentSongProgress =
                        MusicPlayerViewModel.musicService!!.mediaPlayer!!.currentPosition
                    MusicPlayerViewModel.songPlaying.value = 0
                    musicService!!.showNotification(R.drawable.play_pause_button)
                } else {
                    viewModel.startPlaying()
                    musicService!!.showNotification(R.drawable.pause_song_button)
                }
            }


            binding.previousSong -> {
                viewModel.setPreviousNextSong(false)
            }

            binding.nextSong -> {
                viewModel.setPreviousNextSong(true)
            }

        }
    }

    override fun onBackPressed() {
        viewModel.stopPlaying()
        musicService!!.stopForeground(true)
        super.onBackPressed()
    }

    override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
        val binder = service as MusicService.MyBinder
        if (musicService != null) {
            viewModel.stopPlaying()
        }

        musicService = binder.currentService()
        viewModel.audioToMediaPlayerConverter(musicService!!)
        musicService!!.showNotification(R.drawable.pause_song_button)
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        musicService = null
    }
}

