package com.yogi.core.model

import com.google.gson.annotations.SerializedName

data class YtDefaultMdl(@SerializedName("width")
                        val width: Int = 0,
                        @SerializedName("url")
                        val url: String = "",
                        @SerializedName("height")
                        val height: Int = 0)