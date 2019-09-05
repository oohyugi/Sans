package com.yogi.core.model

import com.google.gson.annotations.SerializedName

data class RadioItemsMdl(
                    @SerializedName("streams")
                    val streams: List<RdStreamsItemMdl>?,
                    @SerializedName("station")
                    val station: RdStationMdl
)