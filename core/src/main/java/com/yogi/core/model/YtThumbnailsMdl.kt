package com.yogi.core.model

import com.google.gson.annotations.SerializedName

data class YtThumbnailsMdl(@SerializedName("default")
                           val default: YtDefaultMdl,
                           @SerializedName("high")
                           val high: YtHighMdl,
                           @SerializedName("medium")
                           val medium: YtMediumMdl,
                           @SerializedName("standard")
                           val standard: YtMediumMdl
)