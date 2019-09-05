package com.yogi.core.model

import com.google.gson.annotations.SerializedName

data class RdStreamsItemMdl(@SerializedName("posX")
                            val posX: Int = 0,
                            @SerializedName("posY")
                            val posY: Int = 0,
                            @SerializedName("mime")
                            val mime: String = "",
                            @SerializedName("isContainer")
                            val isContainer: Boolean = false,
                            @SerializedName("mediaType")
                            val mediaType: String = "",
                            @SerializedName("id")
                            val id: Int = 0,
                            @SerializedName("url")
                            val url: String = "")