package com.yogi.player

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelection
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory


class PlayerActivity : AppCompatActivity(), Player.EventListener {

    private var mainHandler: Handler? = null
    private var renderersFactory: RenderersFactory? = null
    private var bandwidthMeter: BandwidthMeter? = null
    private var loadControl: LoadControl? = null
    private var dataSourceFactory: DataSource.Factory? = null
    private var extractorsFactory: ExtractorsFactory? = null
    private var mediaSource: MediaSource? = null
    private var trackSelectionFactory: TrackSelection.Factory? = null
    private var player: SimpleExoPlayer? = null
    private val streamUrl = "http://bbcwssc.ic.llnwd.net/stream/bbcwssc_mp1_ws-einws" //bbc world service url
    private var trackSelector: TrackSelector? = null
    private var urlToPlay = ""

    companion object {
        const val EXTRA_URL = "extra_url"
        fun startThisActivity(context: Context, url: String) {
            val intent = Intent(context, PlayerActivity::class.java)
            intent.apply {
                putExtra(EXTRA_URL, url)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        intent.apply {
            urlToPlay = getStringExtra(EXTRA_URL)
        }
        initializePlayer()
    }

    override fun onResume() {
        super.onResume()
        player?.playWhenReady = true
        Toast.makeText(this, "Exoplayer is playing.", Toast.LENGTH_SHORT).show()
    }

    private fun initializePlayer() {
        renderersFactory = DefaultRenderersFactory(applicationContext)
        bandwidthMeter = DefaultBandwidthMeter()
        trackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
        trackSelector = DefaultTrackSelector(trackSelectionFactory)
        loadControl = DefaultLoadControl()

        player = ExoPlayerFactory.newSimpleInstance(this, renderersFactory, trackSelector)
        player?.addListener(this)

        dataSourceFactory = DefaultDataSourceFactory(applicationContext, "ExoplayerDemo")
        extractorsFactory = DefaultExtractorsFactory()
        mainHandler = Handler()

        val uriString = urlToPlay
        var mp4VideoUri = Uri.parse(uriString)
        var videoSource: MediaSource? = null
        Log.wtf("url", uriString)
//        String filenameArray[] = urlToPlay.split("\\.");
        if (uriString.toUpperCase().contains("M3U8")) {

            videoSource = ExtractorMediaSource(
                Uri.parse(uriString),
                dataSourceFactory,
                extractorsFactory,
                mainHandler,
                null
            )
        } else {
//            mp4VideoUri = Uri.parse("")
            videoSource = ExtractorMediaSource(
                Uri.parse(uriString),
                dataSourceFactory,
                extractorsFactory,
                mainHandler,
                null
            )
        }
//        mediaSource = ExtractorMediaSource(
//            Uri.parse(streamUrl),
//            dataSourceFactory,
//            extractorsFactory,
//            mainHandler,
//            null
//        )

        player?.prepare(videoSource)
    }

    override fun onPlayerError(error: ExoPlaybackException?) {
        Log.wtf("error", error?.message)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        player?.playWhenReady = false
        player?.stop()
    }

}
