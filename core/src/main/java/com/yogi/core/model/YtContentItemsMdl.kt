package com.yogi.core.model

import com.google.gson.annotations.SerializedName

data class YtContentItemsMdl(
    @SerializedName("snippet")
    val snippet: YtSnippetMdl,
    @SerializedName("kind")
    val kind: String = "",
    @SerializedName("etag")
    val etag: String = "",
    @SerializedName("id")
    val id: Any,
    @SerializedName("videoId")
    val videoId:String?=null
)