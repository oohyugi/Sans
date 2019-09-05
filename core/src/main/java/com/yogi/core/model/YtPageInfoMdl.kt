package com.yogi.core.model

import com.google.gson.annotations.SerializedName

data class YtPageInfoMdl(@SerializedName("totalResults")
                         val totalResults: Int = 0,
                         @SerializedName("resultsPerPage")
                         val resultsPerPage: Int = 0)