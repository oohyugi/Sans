package com.yogi.player

import android.os.Bundle
import android.os.Handler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import at.huber.youtubeExtractor.VideoMeta
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.trackselection.TrackSelection
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yogi.core.SharedViewModel
import com.yogi.core.model.ResponseRadioMdl
import com.yogi.core.model.YtIsPlayingNowMdl
import com.yogi.core.model.YtItemsMdl
import com.yogi.core.utils.*
import kotlinx.android.synthetic.main.bottomsheet_player.*


/**
 * Created by oohyugi on 2019-08-25.
 * github: https://github.com/oohyugi
// */
class PlayerBottomSheetDialog(val listener: PlayerDialogListener? = null, val playlistName: String? = null, val playlists:List<YtItemsMdl> = mutableListOf(),val position:Int=0,val type:String?="music") :
    BottomSheetDialogFragment(),PlayerCallBacks.playerCallBack {


    private lateinit var mBehavior: BottomSheetBehavior<FrameLayout>
    private var mainHandler: Handler? = null
    private var renderersFactory: RenderersFactory? = null
    private var bandwidthMeter: BandwidthMeter? = null
    private var loadControl: LoadControl? = null
    private var dataSourceFactory: DataSource.Factory? = null
    private var extractorsFactory: ExtractorsFactory? = null
    private var trackSelectionFactory: TrackSelection.Factory? = null
    private var player: SimpleExoPlayer? = null
    private val streamUrl = "http://bbcwssc.ic.llnwd.net/stream/bbcwssc_mp1_ws-einws" //bbc world service url
    private var trackSelector: TrackSelector? = null
    private var youtubeExtractUrl = ""
    private  var mVideoMeta: VideoMeta? = null
    private var durationSet =false
    private lateinit var mPlaylistItemAdapter : PlaylistItemAdapter
    private lateinit var mPlaylistItemRadioAdapter : PlaylistItemRadioAdapter
    private var realDurationMillis: Long? = 0
    private  var exoPlayerManager: ExoPlayerManager2? = null
    private var playingPosition = 0
    lateinit var sharedViewModel:SharedViewModel
    var lastIdPlaying = ""
    var lastPlayingPos = -1
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.setOnShowListener {
            val d = dialog as BottomSheetDialog
            mBehavior = BottomSheetBehavior.from(d.findViewById(com.google.android.material.R.id.design_bottom_sheet))
            mBehavior.skipCollapsed = true

            mBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomsheetCallback()

        }
        return inflater.inflate(R.layout.bottomsheet_player, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            sharedViewModel = ViewModelProviders.of(it).get(SharedViewModel::class.java)
        }


        tv_title.text = "Now Playing"

        lastPlayingPos = position
        playingPosition = position
        exoPlayerManager = activity?.let { ExoPlayerManager2.with(it) }
        setItemPlaylist()












        exoPlayerManager!!.getPlayer()?.let { setProgress(it) }

    }

    override fun onPlayerReady(
        mPlayer: SimpleExoPlayer?,
        title: String?,
        subTitle: String?,
        playlistIndex: Int
    ) {

//        sharedViewModel.isPlayingNow.postValue(YtIsPlayingNowMdl(playlists,playingPosition))
        exoPlayerManager?.getPlayer()?.let { setProgress(it) }
        super.onPlayerReady(mPlayer, title, subTitle, playlistIndex)
    }




    private fun initMediaController() {
        if (exoPlayerManager?.isPlayerPlaying!!){
            iv_play?.setImageResource(R.drawable.ic_pause_player)

        }else{
            iv_play?.setImageResource(R.drawable.ic_play_player)
        }

        iv_play?.setOnClickListener {
            if (exoPlayerManager?.isPlayerPlaying!!) {
                iv_play?.setImageResource(R.drawable.ic_play_player)
                exoPlayerManager?.pausePlayer()
            } else {
                player?.playWhenReady = true
                iv_play?.setImageResource(R.drawable.ic_pause_player)
                exoPlayerManager?.startPlayer()

            }
        }


        seek_progress.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, fromUser: Boolean) {
                if (!fromUser){
                    return
                }
                exoPlayerManager?.seekTo((p1*1000).toLong())
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })

    }
    private fun setProgress(mPlayer: SimpleExoPlayer) {
        tv_play_title?.text = exoPlayerManager?.getTitle()
        tv_play_subtitle?.text = exoPlayerManager?.getSubTitle()

        seek_progress?.progress = 0
        tv_duration?.text = mPlayer.duration.millisecondToMinute()
        tv_current_duration?.text = mPlayer.currentPosition.millisecondToMinute()
        val handler =  Handler()
        handler.post(object :Runnable{
            override fun run() {
                seek_progress?.max = (mPlayer.duration/1000).toInt()
                seek_progress?.progress = (mPlayer.currentPosition/1000).toInt()
                tv_current_duration?.text = mPlayer.currentPosition.millisecondToMinute()
                handler.postDelayed(this,1000)
            }

        })
        initMediaController()



    }

    @Suppress("UNCHECKED_CAST")
    private fun setItemPlaylist() {
        val mLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        if (type=="music"){
            mPlaylistItemAdapter = PlaylistItemAdapter(activity, playlists as List<YtItemsMdl>)
            rv_playlist.apply {
                layoutManager = mLayoutManager
                adapter = mPlaylistItemAdapter

            }
            mPlaylistItemAdapter.notifyDataSetChanged()
        }else{
            mPlaylistItemRadioAdapter = PlaylistItemRadioAdapter(activity, playlists as List<ResponseRadioMdl>)
            rv_playlist.apply {
                layoutManager = mLayoutManager
                adapter = mPlaylistItemAdapter

            }
            mPlaylistItemAdapter.notifyDataSetChanged()
        }


//        val smoothScroller =  CenterSmoothScroller(rv_playlist.context)
//        smoothScroller.targetPosition = position
//        mLayoutManager.startSmoothScroll(smoothScroller)

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(rv_playlist)

        val snapOnScrollListener = SnapOnScrollListener(
            snapHelper,
            SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL_STATE_IDLE,
            object : OnSnapPositionChangeListener {
                override fun onSnapPositionChange(position: Int) {
//                exoPlayerManager?.extractUrl(playlists[position].snippet.resourceId.videoId)
                    playingPosition = position


                    if (exoPlayerManager?.getPlaylistPosition()!=position) {
                        if (type=="music"){
                            exoPlayerManager?.setPlaylist(
                                YtIsPlayingNowMdl(playlists ,playingPosition,playlistName),
                                playingPosition)
                            tv_play_title?.text = playlists[position].snippet.title
                            tv_play_subtitle?.text = ""


                        }


                    }


                }

            })

        rv_playlist?.addOnScrollListener(snapOnScrollListener)

        exoPlayerManager?.getPlaylistPosition()?.let {

            rv_playlist?.smoothScrollToPosition(it)
        }
    }

    private fun bottomsheetCallback() {
        mBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {

            }

            override fun onStateChanged(view: View, state: Int) {
                if (state == BottomSheetBehavior.STATE_HIDDEN) {
                    listener?.statusShowDialog(false)
                    dialog.dismiss()


                }
                if (state == BottomSheetBehavior.STATE_EXPANDED) {
                    listener?.statusShowDialog(true)
                }
            }

        })

    }




    interface PlayerDialogListener {
        fun statusShowDialog(status: Boolean)
        fun isPlayingNow(playlists: List<YtItemsMdl>, position: Int)
    }

}