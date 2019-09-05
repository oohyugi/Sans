package com.yogi.core.utils

import android.os.Build
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import java.util.concurrent.TimeUnit
import android.content.Context.ACTIVITY_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import android.app.ActivityManager
import android.content.Context
import android.provider.SyncStateContract


/**
 * Created by oohyugi on 2019-08-24.
 * github: https://github.com/oohyugi
 */


fun String.capitalizeWords(): String = split(" ").map { it.capitalize() }.joinToString(" ")


fun Long.millisecondToMinute(): String {

    return String.format(
        "%02d : %02d",
        TimeUnit.MILLISECONDS.toMinutes(this),
        TimeUnit.MILLISECONDS.toSeconds(this) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(this))
    )

}


fun SnapHelper.getSnapPosition(recyclerView: RecyclerView): Int {
    val layoutManager = recyclerView.layoutManager ?: return RecyclerView.NO_POSITION
    val snapView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
    return layoutManager.getPosition(snapView)
}

fun String.getStreamingUrl(): LinkedList<String>? {

    Log.e("get streaming url",this)
    val br: BufferedReader
    var murl: String? = null
    var murls: LinkedList<String>? = null
    try {
        val mUrl = URL(this).openConnection()
        br = BufferedReader(
            InputStreamReader(mUrl.getInputStream())
        )
        murls = LinkedList()
        while (true) {
            try {
                val line = br.readLine() ?: break

                murl = parseLine(line)
                if (murl != null && murl != "") {
                    murls.add(murl)

                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    } catch (e: MalformedURLException) {
        // TODO Auto-generated catch block
        e.printStackTrace()
    } catch (e: IOException) {
        // TODO Auto-generated catch block
        e.printStackTrace()
    }

    Log.wtf("url to stream :",  murl!!)
    return murls
}

private fun parseLine(line: String?): String? {
    if (line == null) {
        return null
    }
    val trimmed = line.trim { it <= ' ' }
    return if (trimmed.indexOf("http") >= 0) {
        trimmed.substring(trimmed.indexOf("http"))
    } else ""
}


fun getDefaultUserAgent(): String {
    val result = StringBuilder(64)
    result.append("Dalvik/")
    result.append(System.getProperty("java.vm.version")) // such as 1.1.0
    result.append(" (Linux; U; Android ")

    val version = Build.VERSION.RELEASE // "1.0" or "3.4b5"
    result.append(if (version.length > 0) version else "1.0")

    // add the model for the release build
    if ("REL" == Build.VERSION.CODENAME) {
        val model = Build.MODEL
        if (model.length > 0) {
            result.append("; ")
            result.append(model)
        }
    }
    val id = Build.ID // "MASTER" or "M4-rc20"
    if (id.length > 0) {
        result.append(" Build/")
        result.append(id)
    }
    result.append(")")
    return result.toString()
}


fun isServiceRunning(context: Context?, serviceClass: Class<*>): Boolean {
    val activityManager = context?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val services = activityManager.getRunningServices(Integer.MAX_VALUE)

    for (runningServiceInfo in services) {
        Log.d("Service", String.format("Service:%s", runningServiceInfo.service.className))
        if (runningServiceInfo.service.className.equals(serviceClass.name)) {
            return true
        }
    }
    return false
}