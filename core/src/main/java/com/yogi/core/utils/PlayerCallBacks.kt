package com.yogi.core.utils

import com.google.android.exoplayer2.SimpleExoPlayer

/**
 * Created by oohyugi on 2019-08-25.
 * github: https://github.com/oohyugi
 */
interface PlayerCallBacks {

    fun callbackObserver(obj: Any)

    interface playerCallBack {
        fun onItemClickOnItem(albumId: Int?){}

        fun onPlayingEnd(){}
        fun onPlayerReady(
            mPlayer: SimpleExoPlayer?,
            title: String?,
            subTitle: String?,
            playlistIndex: Int
        ) {}

         fun onLoadingItem(isLoading: Boolean){}
        fun onStartPlaying(isPlaying: Boolean) {}
    }
}
