package com.yogi.network.repositories

import com.yogi.core.model.RadioItemsMdl
import com.yogi.core.model.ResponseRadioMdl
import com.yogi.network.ApiClient
import com.yogi.network.BuildConfig
import com.yogi.network.base.MyResult

/**
 * Created by oohyugi on 2019-08-24.
 * github: https://github.com/oohyugi
 */

interface RadioRepo {
    suspend fun getListRadio() : MyResult<List<ResponseRadioMdl>>

}


class RadioRepoImpl: RadioRepo{
    var api= ApiClient.makeApiServices(BuildConfig.BASE_URL_RADIO)
    override suspend fun getListRadio(): MyResult<List<ResponseRadioMdl>> {
        return try {
            val result = api.getRadioList().await()
            MyResult.Success(result)
        }catch (e : Exception){
            MyResult.Error(e)

        }
    }



}