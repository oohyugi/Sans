package com.yogi.network.base

import okhttp3.Response
import retrofit2.HttpException

/**
 * Created by oohyugi on 2019-05-01.
 * github: https://github.com/oohyugi
 */
sealed class BaseResult<out T : Any> {
    /**
     * Successful result of request without errors
     */
    class Ok<out T : Any>(
        val value: T,
        override val response: Response
    ) : BaseResult<T>(), ResponseResult {
        override fun toString() = "MyResult.Ok{value=$value, response=$response}"
    }

    /**
     * HTTP error
     */
    class Error(
        override val exception: HttpException,
        override val response: Response
    ) : BaseResult<Nothing>(), ErrorResult,
        ResponseResult {
        override fun toString() = "MyResult.Error{exception=$exception}"
    }

    /**
     * Network exception occurred talking to the server or when an unexpected
     * exception occurred creating the request or processing the response
     */
    class Exception(
        override val exception: Throwable
    ) : BaseResult<Nothing>(), ErrorResult {
        override fun toString() = "MyResult.Exception{$exception}"
    }

}

/**
 * Interface for [MyResult] classes with [okhttp3.Response]: [MyResult.Ok] and [MyResult.Error]
 */
interface ResponseResult {
    val response: Response
}

/**
 * Interface for [MyResult] classes that contains [Throwable]: [MyResult.Error] and [MyResult.Exception]
 */
interface ErrorResult {
    val exception: Throwable
}

/**
 * Returns [MyResult.Ok.value] or `null`
 */
fun <T : Any> BaseResult<T>.getOrNull(): T? =
    (this as? BaseResult.Ok)?.value

/**
 * Returns [MyResult.Ok.value] or [default]
 */
//fun <T : Any> MyResult<T>.getOrDefault(default: T): T =
//    getOrNull() ?: default

/**
 * Returns [MyResult.Ok.value] or throw [throwable] or [ErrorResult.exception]
 */
fun <T : Any> BaseResult<T>.getOrThrow(throwable: Throwable? = null): T {
    return when (this) {
        is BaseResult.Ok -> value
        is BaseResult.Error -> throw throwable ?: exception
        is BaseResult.Exception -> throw throwable ?: exception
    }
}
