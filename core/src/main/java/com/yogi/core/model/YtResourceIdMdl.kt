package com.yogi.core.model

import com.google.gson.annotations.SerializedName

data class YtResourceIdMdl(@SerializedName("kind")
                           val kind: String = "",
                           @SerializedName("videoId")
                           val videoId: String = "")