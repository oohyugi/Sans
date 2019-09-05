package com.yogi.core.model

import com.google.gson.annotations.SerializedName

data class RdStationMdl(@SerializedName("name")
                        val name: String = "",
                        @SerializedName("id")
                        val id: String = "",
                        @SerializedName("title")
                        val title: String = "",
                        @SerializedName("url")
                        val url: String = "",
                        @SerializedName("img")
                        val img: String = "",
                        @SerializedName("usePopup")
                        val usePopup: Boolean = false)