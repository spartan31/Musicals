package com.example.musicals.models.roomdatabase
import java.io.Serializable

class AudioModel (
    val audioPath : String,
    val audioName : String,
    val audioAlbum : String,
    val audioArtist : String,
    val audioImage : String
) : Serializable
