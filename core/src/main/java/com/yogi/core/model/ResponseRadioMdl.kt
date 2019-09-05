package com.yogi.core.model

import com.google.gson.annotations.SerializedName

data class ResponseRadioMdl(@SerializedName("img")
                            val img: String = "",
                            @SerializedName("title")
                            val title: String = "",
                            @SerializedName("items")
                            val items: RadioItemsMdl? = null,
                            @SerializedName("url")
                            val url: String = "")