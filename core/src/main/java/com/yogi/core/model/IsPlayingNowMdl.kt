package com.yogi.core.model

import com.google.gson.annotations.SerializedName

data class IsPlayingNowMdl(
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("subtitle")
    var subtitle: String? = null,
    @SerializedName("url")
    var url: String? = null,
    @SerializedName("thumbnail")
    var thumbnail: String? = null,
    @SerializedName("items")
    var items: List<YtItemsMdl>? = mutableListOf(),
    @SerializedName("type")
    var type: String? = null,
    @SerializedName("position_playing")
    var position: Int = 0,
    @SerializedName("playlist_name")
    var playlistName : String? = null
)