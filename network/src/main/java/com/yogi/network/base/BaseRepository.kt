package com.yogi.network.base

import android.util.Log
import retrofit2.Response
import java.io.IOException

/**
 * Created by oohyugi on 2019-05-04.
 * github: https://github.com/oohyugi
 */
open class BaseRepository {

suspend fun <T : Any> safeApiCall(call: suspend () -> MyResult<T>, errorMessage: String): MyResult<T> = try {
    call.invoke()
} catch (e: Exception) {
    MyResult.Error(IOException(errorMessage, e))
}
}