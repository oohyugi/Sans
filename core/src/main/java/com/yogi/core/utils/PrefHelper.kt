package com.yogi.core.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.yogi.core.model.IsPlayingNowMdl
import com.yogi.core.model.YtIsPlayingNowMdl
import com.yogi.core.model.YtItemsMdl
import com.yogi.core.model.YtResponseSearchMdl

/**
 * Created by oohyugi on 2019-08-24.
 * github: https://github.com/oohyugi
 */
object PrefHelper {

    const val PREF_DISCOVERY="pref_discovery"
    const val PREF_ISPLAYING="pref_isplaying"

    private fun pref(context:Context): SharedPreferences{
        return PreferenceManager.getDefaultSharedPreferences(context)

    }


    fun saveDiscovery(context: Context?,data:YtResponseSearchMdl?){
        context?.let { pref(it).edit().putString(PREF_DISCOVERY,Gson().toJson(data)).apply() }
    }
    fun getDiscovery(context: Context?):YtResponseSearchMdl?{
        val jsonString = context?.let { pref(it).getString(PREF_DISCOVERY,null) }
        return if (jsonString!=null){
            Gson().fromJson(jsonString,YtResponseSearchMdl::class.java)
        }else{
            null
        }

    }
    fun saveIsPlayingContent(context: Context?, data:IsPlayingNowMdl){
        context?.let { pref(it).edit().putString(PREF_ISPLAYING,Gson().toJson(data)).apply() }

    }

    fun getIsplayingContent(context: Context?):IsPlayingNowMdl?{
        val jsonString = context?.let { pref(it).getString(PREF_ISPLAYING,null) }
        return if (jsonString!=null){
            Gson().fromJson(jsonString,IsPlayingNowMdl::class.java)
        }else{
            null
        }

    }
}