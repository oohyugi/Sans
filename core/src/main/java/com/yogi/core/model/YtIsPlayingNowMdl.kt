package com.yogi.core.model

import com.google.gson.annotations.SerializedName

data class YtIsPlayingNowMdl(
    @SerializedName("items")
    var items: List<YtItemsMdl> = mutableListOf(),
    @SerializedName("position_playing")
    var position: Int = 0,
    @SerializedName("playlist_name")
    var playlistName : String? = null
)