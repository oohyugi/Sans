package com.yogi.network.base

/**
 * Created by oohyugi on 2019-05-01.
 * github: https://github.com/oohyugi
 */
sealed class MyResult<out T: Any> {
    data class Success<out T : Any>(val data: T) : MyResult<T>()
    data class Error(val exception: Throwable, val message: String = exception.localizedMessage):MyResult<Nothing>()
}