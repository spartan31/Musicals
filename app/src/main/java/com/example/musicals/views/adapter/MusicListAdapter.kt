package com.example.musicals.views.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.musicals.R
import com.example.musicals.databinding.RecyclerAudioCardBinding
import com.example.musicals.models.roomdatabase.AudioModel
import com.example.musicals.views.activity.MusicPlayerActivity

class MusicListAdapter(val context: Context, private val audioList : ArrayList<AudioModel>) : RecyclerView.Adapter<MusicListAdapter.MusicListViewHolder>() {

    private lateinit var binding : RecyclerAudioCardBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicListViewHolder {

        // Binding
        binding = RecyclerAudioCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MusicListViewHolder(binding)

    }

    override fun onBindViewHolder(holder: MusicListViewHolder, position: Int) {

        with(audioList[position]){
            binding.songName.text = audioList[position].audioName
            binding.songArtist.text = audioList[position].audioArtist
           Glide.with(context).load(audioList[position].audioImage).apply(RequestOptions().placeholder(
                R.drawable.music_icon_foreground)).into(binding.songIcon)
        }

        binding.cardView.setOnClickListener {
            val intent = Intent(context, MusicPlayerActivity::class.java)
            intent.putExtra("audio", audioList[position])
            intent.putExtra("position",position)
            intent.putExtra("songList",audioList)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return  audioList.size
    }

    class MusicListViewHolder(binding: RecyclerAudioCardBinding) : RecyclerView.ViewHolder(binding.root)
}