package com.yogi.core.utils

import android.content.Context
import androidx.recyclerview.widget.LinearSmoothScroller

/**
 * Created by oohyugi on 2019-08-25.
 * github: https://github.com/oohyugi
 */
 class CenterSmoothScroller(context: Context) : LinearSmoothScroller(context) {


    override fun calculateDtToFit(viewStart: Int, viewEnd: Int, boxStart: Int, boxEnd: Int, snapPreference: Int): Int {
        return boxStart + (boxEnd - boxStart) / 2 - (viewStart + (viewEnd - viewStart) / 2)
    }
}
