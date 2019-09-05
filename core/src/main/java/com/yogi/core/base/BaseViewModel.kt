package com.yogi.core.base


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

/**
 * Created by oohyugi on 2019-05-01.
 * github: https://github.com/oohyugi
 */
open class BaseViewModel : ViewModel(),CoroutineScope {

    val showLoading = MutableLiveData<Boolean>()
    val showError = MutableLiveData<String>()
    var myJob = Job()
    override val coroutineContext: CoroutineContext get() = myJob + Dispatchers.Main

    override fun onCleared() {
        super.onCleared()
        myJob.cancel()
    }



}