package com.yogi.network.repositories

import com.yogi.network.ApiClient
import com.yogi.network.BuildConfig
import com.yogi.network.base.MyResult
import com.yogi.core.model.YtItemsMdl
import com.yogi.core.model.YtResponseSearchMdl

/**
 * Created by oohyugi on 2019-08-24.
 * github: https://github.com/oohyugi
 */

interface YoutubeRepo {
    suspend fun getVideo(type:String,region: String,maxResult:Int) : MyResult<YtResponseSearchMdl>
    suspend fun getDiscovery() : MyResult<YtResponseSearchMdl>
    suspend fun getDiscoveryItems(data:YtResponseSearchMdl) : MyResult<YtResponseSearchMdl>

}


class YoutubeRepoImpl: YoutubeRepo{



    private val partSnippet = "snippet"
    private val partContentDetail = "contentDetails"
    private val topTrackIndo = "PLFgquLnL59alQ4PrI-9tZyl0Z8Bqp-RE7&hl"
    private val playlistId = "PLFgquLnL59alQ4PrI-9tZyl0Z8Bqp-RE7," +
            "PLFgquLnL59anIEZe7OonXASn8v_W8UoTW," +
            "PLS_oEMUyvA728OZPmF9WPKjsGtfC75LiN," +
            "PLTDluH66q5mpm-Bsq3GlwjMOHITt2bwXE," +
            "PLDcnymzs18LU4Kexrs91TVdfnplU3I5zs," +
            "PLUg_BxrbJNY5gHrKsCsyon6vgJhxs72AH," +
            "PLH6pfBXQXHEANvXkdNlaofmN-2dOpGTHZ," +
            "PLhd1HyMTk3f5PzRjJzmzH7kkxjfdVoPPj," +
            "PLMcThd22goGYit-NKu2O8b4YMtwSTK9b9," +
            "PLZ7FEm7TjJDtTuSQ6RLRYPEtiq328xbFI," +
            "PLtA1tHJM3n2wei-sy4lKkQRbjLiaMMPP2," +
            "PLMC9KNkIncKtPzgY-5rmhvj7fax8fdxoj," +
            "PLxhnpe8pN3TkenzFLTlz2hUd6_BZu-5Zv," +
            "PL4EDF0448D3DB6031," +
            "PLcirGkCPmbmEgDAsRiu9WyOGCAVEWPwhs," +
            "PL4QNnZJr8sROsru5zqonUp0kzM919agZJ," +
            "PLjB-wurbnZxUY8WEq6iu-A45Bt_EjXvDX," +
            "PLkbaG37V-vG8Dsx0YlrD3nXQIVuutl4hK"
     var api= ApiClient.makeApiServices(BuildConfig.BASE_URL_YOUTUBE)
    override suspend fun getVideo(type: String,region:String, maxResult: Int): MyResult<YtResponseSearchMdl> {
        return try {
            val result = api.getYoutubeVideo(partSnippet,type,region,BuildConfig.API_KEY_GOOGLE,maxResult).await()
            MyResult.Success(result)
        }catch (e : Exception){
            MyResult.Error(e)

        }
    }
    override suspend fun getDiscovery(): MyResult<YtResponseSearchMdl> {

        return try {
            val result = api.getYoutubeDiscovery(partSnippet,playlistId,BuildConfig.API_KEY_GOOGLE).await()
            getDiscoveryItems(result)
        }catch (e : Exception){
            MyResult.Error(e)

        }

    }


    override suspend fun getDiscoveryItems( data: YtResponseSearchMdl): MyResult<YtResponseSearchMdl> {
        val mListDiscovery:MutableList<YtItemsMdl> = mutableListOf()
        for (item in data.items!!){
             try {
                val result = api.getYoutubeDiscoveryItems("$partSnippet,$partContentDetail",item.id as String,10,"",BuildConfig.API_KEY_GOOGLE).await()
                item.contentItems = result.items as MutableList<YtItemsMdl>
            }catch (e : Exception){
                MyResult.Error(e)

            }
            mListDiscovery.add(item)
        }

         data.items=mListDiscovery

        return MyResult.Success(data)

    }



}