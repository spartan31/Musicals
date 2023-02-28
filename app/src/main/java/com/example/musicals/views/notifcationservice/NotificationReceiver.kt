package com.example.musicals.views.notifcationservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.musicals.R
import com.example.musicals.viewmodels.MusicPlayerViewModel
import com.example.musicals.views.activity.MusicPlayerActivity
import kotlin.system.exitProcess

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(p0: Context?, intent: Intent?) {
        when (intent?.action) {
            NotificationApplication.PREVIOUS -> {
                prevNextSong(false,p0)
            }

            NotificationApplication.PLAY -> {
                if (MusicPlayerViewModel.songPlaying.value == 0) {
                    playMusic()
                } else {
                    pauseMusic()
                }
            }
            NotificationApplication.NEXT -> {
                prevNextSong(true,p0)
            }
            NotificationApplication.EXIT -> {
                MusicPlayerActivity.musicService!!.stopForeground(true)
                MusicPlayerActivity.musicService = null
                exitProcess(1)
            }

        }
    }

    private fun playMusic() {
        MusicPlayerViewModel.songPlaying.value = 1
        MusicPlayerViewModel.musicService!!.mediaPlayer!!.start()
        MusicPlayerViewModel.musicService!!.showNotification(R.drawable.pause_song_button)
        MusicPlayerActivity.binding.playPauseButton.setImageResource(R.drawable.pause_song_button)

    }

    private fun pauseMusic() {

        MusicPlayerViewModel.songPlaying.value = 0
        MusicPlayerViewModel.musicService!!.mediaPlayer!!.pause()
        MusicPlayerViewModel.musicService!!.showNotification(R.drawable.play_pause_button)
        MusicPlayerActivity.binding.playPauseButton.setImageResource(R.drawable.play_pause_button)
        MusicPlayerViewModel.curentSongProgress =
            MusicPlayerViewModel.musicService!!.mediaPlayer!!.currentPosition

    }


    private fun prevNextSong(isNext: Boolean, p0: Context?) {
        if (isNext) {
            if (MusicPlayerViewModel.currentSongPosition.value == (MusicPlayerViewModel.audioList.value!!.size) - 1) {
                MusicPlayerViewModel.currentSongPosition.value = 0
            } else {
                MusicPlayerViewModel.currentSongPosition.value = MusicPlayerViewModel.currentSongPosition.value?.plus(1)
            }
        } else {
            if (MusicPlayerViewModel.currentSongPosition.value == 0) {
                MusicPlayerViewModel.currentSongPosition.value = (MusicPlayerViewModel.audioList.value!!.size) - 1
            } else {
                MusicPlayerViewModel.currentSongPosition.value = MusicPlayerViewModel.currentSongPosition.value?.minus(1)
            }
        }

        MusicPlayerViewModel.musicService!!.mediaPlayer!!.stop()
        MusicPlayerViewModel.musicService!!.mediaPlayer = null
        MusicPlayerViewModel.musicService!!.mediaPlayer = MediaPlayer.create(
            p0, Uri.parse(MusicPlayerViewModel.audioList.value!![MusicPlayerViewModel.currentSongPosition.value!!].audioPath)
        )
        MusicPlayerActivity.binding.songDuration.text = convertDurationToMinutesString(MusicPlayerViewModel.musicService!!.mediaPlayer!!.duration)
        MusicPlayerActivity.binding.songName.apply {
            text = MusicPlayerViewModel.audioList.value!![MusicPlayerViewModel.currentSongPosition.value!!].audioName
            isSelected = true
        }

        MusicPlayerActivity.binding.songArtist.apply {
            text = MusicPlayerViewModel.audioList.value!![MusicPlayerViewModel.currentSongPosition.value!!].audioArtist
            isSelected = true
        }

        if (p0 != null) {
            Glide.with(p0)
                .load(Uri.parse(MusicPlayerViewModel.audioList.value!![MusicPlayerViewModel.currentSongPosition.value!!].audioImage)).apply(
                    RequestOptions().placeholder(R.drawable.default_song_thumbnail)
                ).into(MusicPlayerActivity.binding.imageView)
        }

        playMusic()
        MusicPlayerViewModel.musicService!!.showNotification(R.drawable.pause_song_button)

    }

    fun convertDurationToMinutesString(duration: Int): String {
        val minutes = duration / 1000 / 60
        val seconds = (duration / 1000 % 60).toString().padStart(2, '0')
        return "$minutes:$seconds"
    }
}