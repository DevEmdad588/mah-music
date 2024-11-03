package com.example.mah_music

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//Main Data class also named as POJO class
@Parcelize
data class Data(
    val album: Album,
    val artist: Artist,
    val duration: Int,
    val explicit_content_cover: Int,
    val explicit_content_lyrics: Int,
    val explicit_lyrics: Boolean,
    val id: Long,
    val link: String,
    val md5_image: String,
    val preview: String,
    val rank: Int,
    val readable: Boolean,
    val title: String,
    val title_short: String,
    val title_version: String,
    val type: String
) : Parcelable