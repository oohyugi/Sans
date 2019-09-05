package com.yogi.core.model

import com.google.gson.annotations.SerializedName

data class YtSnippetMdl(@SerializedName("publishedAt")
                        val publishedAt: String = "",
                        @SerializedName("description")
                        val description: String = "",
                        @SerializedName("title")
                        val title: String = "",
                        @SerializedName("thumbnails")
                        val thumbnails: YtThumbnailsMdl,
                        @SerializedName("artist")
                        val artist: String,
                        @SerializedName("channelId")
                        val channelId: String = "",
                        @SerializedName("channelTitle")
                        val channelTitle: String = "",
                        @SerializedName("liveBroadcastContent")
                        val liveBroadcastContent: String = "",
                        @SerializedName("resourceId")
                        val resourceId: YtResourceIdMdl )