package com.yogi.sans

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.yogi.account.AccountFragment
import com.yogi.core.SharedViewModel
import com.yogi.core.base.BaseActivity
import com.yogi.core.model.IsPlayingNowMdl
import com.yogi.core.model.YtIsPlayingNowMdl
import com.yogi.core.model.YtItemsMdl
import com.yogi.core.utils.ExoPlayerManager2
import com.yogi.core.utils.PlayerCallBacks
import com.yogi.core.utils.PrefHelper
import com.yogi.core.utils.switch
import com.yogi.home.HomeFragment
import com.yogi.player.PlayerBottomSheetDialog
import com.yogi.radio.RadioFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(), PlayerCallBacks.playerCallBack {


    private val TAG = "tag"
    private var lastActiveFragment: Fragment = HomeFragment.newInstance()
    var playerDialogFrag: BottomSheetDialogFragment? = null
    lateinit var sharedViewModel: SharedViewModel
    val mPlayList = mutableListOf<YtItemsMdl>()
    var mPositionItemPlaylist = 0
    lateinit var exoPlayerManager: ExoPlayerManager2
    private var mPlayListName: String? = null
    private var isPlayingNow: IsPlayingNowMdl? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedViewModel = ViewModelProviders.of(this).get(SharedViewModel::class.java)

        exoPlayerManager = ExoPlayerManager2.with(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        supportFragmentManager.switch(R.id.main_container, HomeFragment.newInstance(), "home")
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        if (PrefHelper.getIsplayingContent(this) != null) {
            isPlayingNow = PrefHelper.getIsplayingContent(this)
            mPositionItemPlaylist = isPlayingNow!!.position


            iv_act_play?.visibility = View.VISIBLE
            PrefHelper.getIsplayingContent(this)!!.items?.let { mPlayList.addAll(it) }
            initShortcutPlayer(isPlayingNow)
            iv_act_play?.setOnClickListener {
                Log.wtf("urlplay", "url: ${isPlayingNow?.url}")
                if (isPlayingNow?.type == "music") startPlayList()
                else {

                    exoPlayerManager.playRadio(
                        isPlayingNow?.url,
                        isPlayingNow?.title,
                        "radio",
                        isPlayingNow?.thumbnail
                    )


                }
            }


        }

        initViewModel()

        ll_title?.setOnClickListener {

            showPlayer()

        }

        exoPlayerManager.setPlayerListener(this)

    }

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    supportFragmentManager.switch(
                        R.id.main_container,
                        HomeFragment.newInstance(),
                        "home"
                    )
                    lastActiveFragment = HomeFragment.newInstance()

                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_radio -> {

                    supportFragmentManager.switch(
                        R.id.main_container,
                        RadioFragment.newInstance(),
                        "radio"
                    )
                    lastActiveFragment = RadioFragment.newInstance()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_search -> {

                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_my_music -> {
                    supportFragmentManager.switch(
                        R.id.main_container,
                        AccountFragment.newInstance(),
                        "account"
                    )
                    lastActiveFragment = AccountFragment.newInstance()

                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }


    override fun onStartPlaying(isPlaying: Boolean) {

        super.onStartPlaying(isPlaying)
    }
    override fun onPlayerReady(
        mPlayer: SimpleExoPlayer?,
        title: String?,
        subTitle: String?,
        playlistIndex: Int
    ) {

            Glide.with(this@MainActivity)
                .load(exoPlayerManager.getUrlImg())
                .into(iv_artist)


        tv_title?.text = exoPlayerManager.getTitle()
        tv_subtitle?.text = exoPlayerManager.getSubTitle()

        setControllerPlayer(mPlayer?.playWhenReady)
        super.onPlayerReady(mPlayer, title, subTitle, playlistIndex)

    }

    override fun onLoadingItem(isLoading: Boolean) {
        progressplay?.visibility = if (isLoading) View.VISIBLE else View.GONE
        iv_act_play?.visibility = if (!isLoading) View.VISIBLE else View.GONE
        super.onLoadingItem(isLoading)
    }

    private fun initViewModel() {
        sharedViewModel.startPlaying.observe(this, Observer {
            mPlayList.clear()
            mPlayList.addAll(it.items)
            mPositionItemPlaylist = it.position
            startPlayList()
        })
        sharedViewModel.startPlayingRadio.observe(this, Observer {


            for (item in it.items?.streams!!) {
                if (item.mediaType.toLowerCase() == "mp3" || item.mediaType.toLowerCase() == "aac" || item.mediaType.toLowerCase() == "flash") {
                    Log.wtf("dataradio", Gson().toJson(item))
                    exoPlayerManager.playRadio(item.url, it.title, "radio", it.img)
                    initShortcutPlayer(
                        IsPlayingNowMdl(
                            exoPlayerManager.getTitle(),
                            exoPlayerManager.getSubTitle(),
                            exoPlayerManager.getUrl(),
                            mPlayList[mPositionItemPlaylist].snippet.thumbnails.default.url,
                            mPlayList,
                            "music",
                            mPositionItemPlaylist
                        )
                    )

                    return@Observer
                } else {

                }
            }


        })

        sharedViewModel.isPlayingNow.observe(this, Observer {

            mPlayList.clear()
            mPlayList.addAll(exoPlayerManager.getPlaylist())
            mPositionItemPlaylist = exoPlayerManager.getPlaylistPosition()
            Log.wtf("playlist", mPlayList[mPositionItemPlaylist].snippet.title)
            initShortcutPlayer(
                IsPlayingNowMdl(
                    mPlayList[mPositionItemPlaylist].snippet.title,
                    exoPlayerManager.getSubTitle(),
                    exoPlayerManager.getUrl(),
                    mPlayList[mPositionItemPlaylist].snippet.thumbnails.default.url,
                    mPlayList,
                    "music",
                    mPositionItemPlaylist
                )
            )
        })

    }

    private fun initShortcutPlayer(playingNow: IsPlayingNowMdl?) {
        if (isPlayingNow != null) {
            Glide.with(this).load(playingNow?.thumbnail).into(iv_artist)
            tv_title?.text = isPlayingNow?.title
            tv_subtitle?.text = isPlayingNow?.subtitle
        }
//        setControllerPlayer()

    }

    private fun startPlayList() {
        exoPlayerManager.setPlaylist(
            YtIsPlayingNowMdl(
                mPlayList,
                mPositionItemPlaylist,
                mPlayListName
            ), mPositionItemPlaylist
        )


    }


    private fun setControllerPlayer(playing: Boolean?=true) {

        progressplay?.visibility = View.GONE
        if (playing!!){
            iv_act_play?.setImageResource(R.drawable.ic_pause)
        }else{
            iv_act_play?.setImageResource(R.drawable.ic_play)
        }
//
        iv_act_play?.setOnClickListener {

            if (playing!!) {
                iv_act_play?.setImageResource(R.drawable.ic_play)
                exoPlayerManager.pausePlayer()

            } else {
                iv_act_play?.setImageResource(R.drawable.ic_pause)
                exoPlayerManager.startPlayer()
            }


        }


    }

    fun showPlayer() {
        if (mPlayList.isNotEmpty() && exoPlayerManager.getType()=="music") {
            playerDialogFrag = PlayerBottomSheetDialog(
                playlistName = exoPlayerManager.getPlaylistName(),
                playlists = mPlayList,
                position = exoPlayerManager.getPlaylistPosition()

            )
            playerDialogFrag?.show(supportFragmentManager, playerDialogFrag?.tag)
        }


    }


}
