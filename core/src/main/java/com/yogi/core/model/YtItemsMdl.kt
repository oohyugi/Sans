package com.yogi.core.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class YtItemsMdl(@SerializedName("snippet")
                      val snippet: YtSnippetMdl,
                      @SerializedName("kind")
                      val kind: String = "",
                      @SerializedName("etag")
                      val etag: String = "",
                      @SerializedName("id")
                      val id: Any,
                      @SerializedName("contentItems")
                      var contentItems:MutableList<YtItemsMdl> = mutableListOf()
) : Serializable