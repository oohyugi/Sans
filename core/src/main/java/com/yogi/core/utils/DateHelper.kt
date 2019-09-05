package com.yogi.core.utils

import android.annotation.SuppressLint
import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by oohyugi on 2019-08-24.
 * github: https://github.com/oohyugi
 */



object DateHelper {


    const val DATE_FORMAT_V1 = "yyyy-MM-dd" // 2012-07-30
}
@SuppressLint("SimpleDateFormat")
fun Context?.getDateNow(format:String):String{
    val dateFormat = SimpleDateFormat(format)
    return dateFormat.format(Date())
}