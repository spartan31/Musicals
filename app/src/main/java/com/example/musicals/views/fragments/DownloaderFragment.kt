package com.example.musicals.views.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicals.databinding.FragmentDownloaderBinding
import com.example.musicals.models.roomdatabase.AudioModel
import com.example.musicals.views.adapter.MusicListAdapter


class DownloaderFragment : Fragment() {

    private lateinit var binding: FragmentDownloaderBinding
    private lateinit var musicListAdapter: MusicListAdapter
    private lateinit var contextI: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDownloaderBinding.inflate(inflater, container, false)
        binding.recyclerAudioList.layoutManager = LinearLayoutManager(contextI)

        getAllSongs()
        return binding.root

    }

    private fun getAllSongs() {
        val allAudio: ArrayList<AudioModel> = getAllAudioFromDevice()
        musicListAdapter = MusicListAdapter(contextI, allAudio)
        binding.recyclerAudioList.adapter = musicListAdapter
    }

    private fun getAllAudioFromDevice(): ArrayList<AudioModel> {
        val tempAudioList = ArrayList<AudioModel>()
        val internalContentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        var projection = arrayOf(
            MediaStore.Audio.AudioColumns.DATA,
            MediaStore.Audio.AudioColumns.ALBUM,
            MediaStore.Audio.ArtistColumns.ARTIST,
            MediaStore.Audio.Media.ALBUM_ID
        )
        var cursor =
            contextI.contentResolver.query(internalContentUri, projection, null, null, null)

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val path = cursor.getString(0)
                val album = cursor.getString(1)
                val artist = cursor.getString(2)
                val image = cursor.getLong(3).toString()
                val uri = Uri.parse("content://media/external/audio/albumart")
                val songImageUri = Uri.withAppendedPath(uri,image).toString()
                val name = path.substring(path.lastIndexOf("/") + 1)
                var audioFile = AudioModel(path, name, album, artist,songImageUri)
                tempAudioList.add(audioFile)
            }
            cursor.close()
        }
        return tempAudioList
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contextI = context
    }

}