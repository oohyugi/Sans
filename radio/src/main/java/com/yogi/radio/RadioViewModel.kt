package com.yogi.radio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yogi.core.base.BaseViewModel
import com.yogi.core.model.RadioItemsMdl
import com.yogi.core.model.ResponseRadioMdl
import com.yogi.network.base.MyResult
import com.yogi.network.repositories.RadioRepoImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RadioViewModel : BaseViewModel() {
    // TODO: Implement the ViewModel
    val radioRepo = RadioRepoImpl()
    private val apiListRadio = MutableLiveData<List<ResponseRadioMdl>>()
    fun getResponseApiListRadio(): LiveData<List<ResponseRadioMdl>>{

        loadRadioList()

        return apiListRadio
    }

    private fun loadRadioList() {
        showLoading.value = true
        launch {
            val result = withContext(Dispatchers.IO) {
                radioRepo.getListRadio()
            }
            showLoading.value = false
            when (result) {
                is MyResult.Success -> {
                    apiListRadio.value = result.data
                }
                is MyResult.Error -> {
                    showError.value = result.exception.message
                }
            }
        }

    }


}
