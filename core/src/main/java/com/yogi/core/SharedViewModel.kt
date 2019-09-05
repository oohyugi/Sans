package com.yogi.core

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yogi.core.model.RdStreamsItemMdl
import com.yogi.core.model.ResponseRadioMdl
import com.yogi.core.model.YtIsPlayingNowMdl

/**
 * Created by oohyugi on 2019-08-25.
 * github: https://github.com/oohyugi
 */
class SharedViewModel : ViewModel() {
    val startPlayingRadio = MutableLiveData<ResponseRadioMdl>()
    val isPlayingNow = MutableLiveData<YtIsPlayingNowMdl>()
    val startPlaying = MutableLiveData<YtIsPlayingNowMdl>()

}