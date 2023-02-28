package com.example.musicals.views.notifcationservice

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.provider.MediaStore
import androidx.core.app.NotificationCompat
import com.example.musicals.R
import com.example.musicals.viewmodels.MusicPlayerViewModel

class MusicService : Service() {

    private var myBinder = MyBinder()
    var mediaPlayer: MediaPlayer? = null

    override fun onBind(intent: Intent): IBinder? {
        return myBinder
    }

    inner class MyBinder : Binder() {
        fun currentService(): MusicService {
            return this@MusicService
        }
    }

    fun showNotification(pauseSongButton: Int) {
        val prevIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(NotificationApplication.PREVIOUS)
        val prevIntentPending = PendingIntent.getBroadcast(baseContext,0,prevIntent,PendingIntent.FLAG_IMMUTABLE)

        val playIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(NotificationApplication.PLAY)
        val playIntentPending = PendingIntent.getBroadcast(baseContext,0,playIntent,PendingIntent.FLAG_IMMUTABLE)

        val nextIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(NotificationApplication.NEXT)
        val nextIntentPending = PendingIntent.getBroadcast(baseContext,0,nextIntent,PendingIntent.FLAG_IMMUTABLE)

        val exitIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(NotificationApplication.EXIT)
        val exitIntentPending = PendingIntent.getBroadcast(baseContext,0,exitIntent,PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(baseContext, NotificationApplication.CHANNEL_ID)
            .setContentTitle(MusicPlayerViewModel.currentSongPosition.value?.let {
                MusicPlayerViewModel.audioList.value!![it].audioName
            })
            .setContentText(MusicPlayerViewModel.currentSongPosition.value?.let {
                MusicPlayerViewModel.audioList.value!![it].audioArtist
            })
            .setSmallIcon(R.drawable.music_icon_foreground)
            .setLargeIcon(MediaStore.Images.Media.getBitmap(contentResolver, Uri.parse(MusicPlayerViewModel.currentSongPosition.value?.let {
                MusicPlayerViewModel.audioList.value!![it].audioImage
            })))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC).setOnlyAlertOnce(true)
            .setOnlyAlertOnce(true)
            .addAction(R.drawable.previous_song_button, "Previous", prevIntentPending)
            .addAction(pauseSongButton, "Play", playIntentPending)
            .addAction(R.drawable.play_next_button, "Next", nextIntentPending)
            .addAction(R.drawable.cancel_music_icon, "Exit", exitIntentPending)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0,1,2,3)).build()


        startForeground(13, notification)
    }

}
