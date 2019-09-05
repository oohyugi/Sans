package com.yogi.network

import com.yogi.core.model.RadioItemsMdl
import com.yogi.core.model.ResponseRadioMdl
import com.yogi.core.model.YtResponseSearchMdl
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by oohyugi on 2019-08-24.
 * github: https://github.com/oohyugi
 */
interface ApiServices {

    @GET("videos")
    fun getYoutubeVideo(
        @Query("part") part: String,
        @Query("chart") chart: String,
        @Query("regionCode") regionCode: String,
        @Query("key") key: String,
        @Query("maxResults") maxResults: Int
    ): Deferred<YtResponseSearchMdl>

    @GET("playlistItems")
    fun getVideoPlaylist(
        @Query("part") part: String,
        @Query("playlistId") playlistId: String,
        @Query("key") key: String,
        @Query("maxResults") maxResults: Int
    ): Deferred<YtResponseSearchMdl>

    @GET("playlists")
    fun getYoutubeDiscovery(
        @Query("part") part: String,
        @Query("id") id: String,
        @Query("key") key: String
    ): Deferred<YtResponseSearchMdl>

    @GET("playlistItems")
    fun getYoutubeDiscoveryItems(
        @Query("part") part: String,
        @Query("playlistId") id: String,
        @Query("maxResults") maxResults: Int,
        @Query("pageToken") pageToken: String,
        @Query("key") key: String
    ): Deferred<YtResponseSearchMdl>

    @GET("radio-home.json")
    fun getRadioList(): Deferred<MutableList<ResponseRadioMdl>>

}