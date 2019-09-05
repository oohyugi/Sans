package com.yogi.core.utils

/**
 * Created by oohyugi on 2019-08-25.
 * github: https://github.com/oohyugi
 */

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import android.util.SparseArray
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.extractor.ts.DefaultTsPayloadReaderFactory.FLAG_ALLOW_NON_IDR_KEYFRAMES
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.DefaultHlsExtractorFactory
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.gson.Gson
import com.yogi.core.model.IsPlayingNowMdl
import com.yogi.core.model.PlayerMetaMdl
import com.yogi.core.model.YtIsPlayingNowMdl
import com.yogi.core.model.YtItemsMdl
import com.yogi.core.services.AudioPlayerService
import java.util.*


/**
 * private constructor
 * @param mContext
 */
class ExoPlayerManager2(context: Context) {


    private var mPlayer: SimpleExoPlayer? = null
    var mPlayerNotificationManager: PlayerNotificationManager? = null
    var mMediaSessionConnector: MediaSessionConnector? = null
    var mediaSession: MediaSessionCompat? = null
    var mListAudio: MutableList<YtItemsMdl> = arrayListOf()
    var dataSourceFactory: DefaultDataSourceFactory? = null
    var concatenatingMediaSource: ConcatenatingMediaSource? = null
    private var uriString = ""
    var mPlayList: ArrayList<String>? = arrayListOf()
    var playlistIndex: Int = 0
    var listner: PlayerCallBacks.playerCallBack? = null
    val isPlayerPlaying: Boolean?
        get() = mPlayer?.playWhenReady

    val duration: Long?
        get() = mPlayer?.duration
    val currentDuration: Long?
        get() = mPlayer?.contentDuration
    var mContext: Context? = null
    var mVideoMeta: VideoMeta? = null
    private var playlistName: String? = null
    private var mSubTitle: String? = null
    private var mUrl: String? = null
    private var mType: String = "radio"
    private var mTitle: String? = null
    private var mUrlImg: String? = null
    private var playerMeta: PlayerMetaMdl? = null

    init {


        val bandwidthMeter = DefaultBandwidthMeter()
        mContext = context
        defaultParam(context)




        mPlayer?.addListener(object : Player.EventListener {
            override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {
                Log.i(TAG, "onTimelineChanged: ")


            }

            override fun onTracksChanged(
                trackGroups: TrackGroupArray?,
                trackSelections: TrackSelectionArray?
            ) {
                Log.i(TAG, "onTracksChanged: ")
            }

            override fun onLoadingChanged(isLoading: Boolean) {

                Log.i(TAG, "onLoadingChanged: ")
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                Log.i(TAG, "onPlayerStateChanged: ")
                if (playbackState == 4 && mPlayList != null && playlistIndex + 1 < mPlayList!!.size) {
                    Log.e(TAG, "Song Changed...")

                    playlistIndex++
                    listner!!.onItemClickOnItem(playlistIndex)
                    mTitle = mListAudio[playlistIndex].snippet.title
                    mUrlImg = mListAudio[playlistIndex].snippet.thumbnails.default.url
                    extractUrl(mPlayList!![playlistIndex], mType)
                } else if (playbackState == 4 && mPlayList != null && playlistIndex + 1 == mPlayList!!.size) {
                    mPlayer?.playWhenReady = false
                }
                if (playbackState == ExoPlayer.STATE_ENDED && listner != null) {
                    listner!!.onPlayingEnd()
                }

                if (playbackState == ExoPlayer.STATE_READY) {
                    listner?.onPlayerReady(mPlayer, getTitle(),getSubTitle(),playlistIndex)
                    listner?.onLoadingItem(false)
                }
//                if (playbackState == ExoPlayer.STATE_READY && playWhenReady) {
//                    listner?.onPlayerReady(mPlayer, getTitle(),getSubTitle(),playlistIndex)
//                }


            }

            override fun onRepeatModeChanged(repeatMode: Int) {
                Log.i(TAG, "onRepeatModeChanged: ")
            }

            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                Log.i(TAG, "onShuffleModeEnabledChanged: ")
            }

            override fun onPlayerError(error: ExoPlaybackException?) {
                Log.i(TAG, "onPlayerError: ${error?.message}")
                destroyPlayer()
                defaultParam(context)


            }

            override fun onPositionDiscontinuity(reason: Int) {
                Log.i(TAG, "onPositionDiscontinuity: ")

                if (reason ==Player.DISCONTINUITY_REASON_PERIOD_TRANSITION){

                }
            }

            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
                Log.i(TAG, "onPlaybackParametersChanged: ")
            }

            override fun onSeekProcessed() {
                Log.i(TAG, "onSeekProcessed: ")
            }
        })


    }

    @SuppressLint("StaticFieldLeak")
    private fun decodeStreamLink(url: String?) {
        object : StreamLinkDecoder(url) {

            override fun onPostExecute(s: String) {
                super.onPostExecute(s)

                playStream(s)
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }



    private fun defaultParam(mContext: Context) {
        mPlayer = ExoPlayerFactory.newSimpleInstance(this.mContext, DefaultTrackSelector())


        val userAgent =
            Util.getUserAgent(mContext, "ExoPlayerDemo").replace("ExoPlayerLib", "Blah");
        dataSourceFactory = DefaultDataSourceFactory(
            mContext, userAgent
        )
    }


    fun setPlayerListener(mPlayerCallBack: PlayerCallBacks.playerCallBack) {
        listner = mPlayerCallBack
    }

    fun extractUrl(videoId: String, mType: String) {
        listner?.onLoadingItem(true)
        val baseUrl = "http://youtube.com/watch?v=$videoId"
        val mExtractor = @SuppressLint("StaticFieldLeak")
        object : YouTubeExtractor(mContext!!) {
            override fun onExtractionComplete(
                ytFiles: SparseArray<YtFile>?,
                videoMeta: VideoMeta?
            ) {
                if (ytFiles != null) {

                    Log.wtf("ytFile", Gson().toJson(ytFiles))
                    mVideoMeta = videoMeta
                    mSubTitle = videoMeta?.author
                    playStream(ytFiles[18].url)


                }
            }

        }
        mExtractor.extract(baseUrl, true, true)

    }

    fun playRadio(
        url: String?,
        title: String?,
        subTitle: String?,
        urlImg: String?
    ) {
        mType = "radio"
        mTitle = title
        mSubTitle = subTitle
        mUrlImg = urlImg
        listner?.onLoadingItem(true)
        if (url!!.contains(".pls")) {
            decodeStreamLink(url)
        }else{
            playStream(url)
        }


    }
    fun playStream(urlToPlay: String?) {

        if (urlToPlay != null) {
            uriString = urlToPlay
        }
        mUrl = urlToPlay

        var mp4VideoUri = Uri.parse(uriString)

        Log.wtf("url_play", mUrl)


            concatenatingMediaSource = ConcatenatingMediaSource()
            concatenatingMediaSource?.addMediaSource(buildMediaSource(mp4VideoUri, null))


            val audioAttributes = AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.CONTENT_TYPE_MUSIC)
                .build()
            mPlayer?.audioAttributes = audioAttributes
            // Prepare the player with the source.
            mPlayer?.prepare(concatenatingMediaSource)
            mPlayer?.playWhenReady = true

            listner?.onStartPlaying(true)




        PrefHelper.saveIsPlayingContent(
            mContext,
            IsPlayingNowMdl(
                getTitle(),
                getSubTitle(),
                getUrl(),
                getUrlImg(),
                getPlaylist(),
                getType(),
                playlistIndex
            )
        )

        if (!isServiceRunning(mContext,AudioPlayerService::class.java)){
            AudioPlayerService.startThisService(mContext)
        }


    }

    private fun buildMediaSource(uri: Uri, overrideExtension: String?): MediaSource {
//        val downloadRequest = mContext.getDownloadTracker().getDownloadRequest(uri)
//        if (downloadRequest != null) {
//            return DownloadHelper.createMediaSource(downloadRequest!!, dataSourceFactory)
//        }

        return when ( Util.inferContentType(uri, overrideExtension)) {
            C.TYPE_DASH -> DashMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
            C.TYPE_SS -> SsMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
            C.TYPE_HLS -> {
                val defaultHlsExtractorFactory =
                    DefaultHlsExtractorFactory(FLAG_ALLOW_NON_IDR_KEYFRAMES, true)
                HlsMediaSource.Factory(dataSourceFactory)
                    .setExtractorFactory(defaultHlsExtractorFactory).createMediaSource(uri)
            }
            C.TYPE_OTHER -> ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri)
            else -> throw IllegalStateException("Unsupported type")
        }
    }


    fun getPlayer(): SimpleExoPlayer? {
        return mPlayer
    }

    fun setPlayerVolume(vol: Float) {
        mPlayer?.volume = vol
    }

    fun setUriString(uri: String) {
        uriString = uri
    }

    fun setPlaylist(
        data: YtIsPlayingNowMdl,
        index: Int) {
        mPlayList?.clear()
        mListAudio.clear()
        mListAudio.addAll(data.items)
        playlistName = data.playlistName
        mType = "music"
        mTitle = data.items[index].snippet.title
        mUrlImg = data.items[index].snippet.thumbnails.default.url
        for (item in mListAudio) {
            mPlayList?.add(item.snippet.resourceId.videoId)

        }
        if (index != null) {
            playlistIndex = index
        }

//        for (item in mListAudio){
//            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
//                .setTag(item.snippet.resourceId.videoId)
//                .createMediaSource(Uri.parse(item.snippet.resourceId.videoId))
//            concatenatingMediaSource?.addMediaSource(mediaSource)
//            concatenatingMediaSource?.addMediaSource(buildMediaSource(Uri.parse(item.snippet.resourceId.videoId),null))
//        }

//        playStreamPlaylist((mPlayList)
        extractUrl(mPlayList!![playlistIndex], mType)
    }

    private fun playStreamPlaylist(mPlayList: ArrayList<String>?) {
//        concatenatingMediaSource = ConcatenatingMediaSource()





    }


    fun getPlaylist(): List<YtItemsMdl> {
        return mListAudio
    }

    fun getPlayerMeta(): PlayerMetaMdl{

        return PlayerMetaMdl("",getTitle(),getSubTitle(),"",getUrlImg(),getType())
    }

    fun getTitle(): String? {
        return mTitle
    }

    fun getPlaylistName(): String? {
        return playlistName
    }

    fun getSubTitle(): String? {
        return mSubTitle
    }

    fun getCurrentPlayingItem(): YtItemsMdl {
        return mListAudio[playlistIndex]
    }

    fun getPlaylistPosition(): Int {
        return playlistIndex
    }

    fun getUrl(): String? {
        return mUrl
    }
    fun getUrlImg(): String? {
        return mUrlImg
    }

    fun getType(): String {
        return mType
    }


    fun playerPlaySwitch() {
        if (uriString !== "") {
            mPlayer?.playWhenReady = !mPlayer!!.playWhenReady
        }
    }

    fun stopPlayer(state: Boolean) {
        mPlayer?.playWhenReady = !state
    }

    fun seekTo(position: Long) {
        mPlayer?.seekTo(position)
    }

    fun pausePlayer() {
        mPlayer?.playWhenReady = false
        mPlayer?.playbackState
    }

    fun startPlayer() {
        mPlayer?.playWhenReady = true
        mPlayer?.playbackState
    }

    fun destroyPlayer() {
        mPlayer?.release()
        mPlayer = null
        dataSourceFactory = null
        mPlayerNotificationManager = null
        mediaSession = null
        concatenatingMediaSource = null


    }

    companion object {
        private val TAG = "ExoPlayerManager"

        @SuppressLint("StaticFieldLeak")
        private var mInstance: ExoPlayerManager2? = null

        fun with(mContext: Context): ExoPlayerManager2 {
            if (mInstance == null) {
                mInstance = ExoPlayerManager2(mContext)
            }
            return mInstance as ExoPlayerManager2
        }
    }


}
