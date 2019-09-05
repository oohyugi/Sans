package com.yogi.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yogi.core.base.BaseViewModel
import com.yogi.network.base.MyResult
import com.yogi.core.model.YtItemsMdl
import com.yogi.core.model.YtResponseSearchMdl
import com.yogi.core.utils.PrefHelper
import com.yogi.network.repositories.YoutubeRepoImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : BaseViewModel() {

    val ytResponsesDiscovery = MutableLiveData<YtResponseSearchMdl>()
    val youtubeRepo = YoutubeRepoImpl()
    fun getYtResponseDiscovery(): LiveData<YtResponseSearchMdl> {


        loadYoutubeDiscoveryFromServer()



        return ytResponsesDiscovery

    }


    private fun loadYoutubeDiscoveryFromServer() {
        showLoading.value = true
        launch {
            val result = withContext(Dispatchers.IO) {
                youtubeRepo.getDiscovery()
            }
            showLoading.value = false
            when (result) {
                is MyResult.Success -> {
                    ytResponsesDiscovery.value = result.data
                }
                is MyResult.Error -> {
                    showError.value = result.exception.message
                }
            }
        }
    }

}
