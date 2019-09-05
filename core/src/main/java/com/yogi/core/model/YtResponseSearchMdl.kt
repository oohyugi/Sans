package com.yogi.core.model

import com.google.gson.annotations.SerializedName

data class YtResponseSearchMdl(@SerializedName("regionCode")
                               val regionCode: String = "",
                               @SerializedName("kind")
                               val kind: String = "",
                               @SerializedName("nextPageToken")
                               val nextPageToken: String = "",
                               @SerializedName("pageInfo")
                               val pageInfo: YtPageInfoMdl,
                               @SerializedName("etag")
                               val etag: String = "",
                               @SerializedName("items")
                               var items: List<YtItemsMdl>?,
                               @SerializedName("lastUpdate")
                               var lastUpdate:String? =null
)