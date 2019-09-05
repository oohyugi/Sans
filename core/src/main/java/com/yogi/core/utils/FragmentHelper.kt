package com.yogi.core.utils

import android.content.Context
import android.util.TypedValue
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

/**
 * Created by oohyugi on 2019-08-23.
 * github: https://github.com/oohyugi
 */

fun FragmentActivity.replaceFragment(fragment: Fragment, idContainer: Int, tag: String?) {
    supportFragmentManager.beginTransaction()
            .replace(idContainer,fragment,tag)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .addToBackStack(tag)
            .commit()

}
fun FragmentActivity.replaceFragmentAndHide(fragment: Fragment, idContainer: Int, tag: String?,hideFragment:Fragment) {
    supportFragmentManager.beginTransaction()
        .add(idContainer,fragment,tag)
        .hide(hideFragment)
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        .commit()

}
fun FragmentActivity.addFragment(fragment: Fragment, idContainer: Int, tag: String?) {
    supportFragmentManager.beginTransaction()
            .add(idContainer,fragment,tag)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()

}

fun Context.convertDip2Pixels(dip: Int): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip.toFloat(), this.resources.displayMetrics)
        .toInt()
}

fun FragmentManager.switch(containerId: Int, newFrag: Fragment, tag: String) {

    var current = findFragmentByTag(tag)
    beginTransaction()
        .apply {

            //Hide the current fragment
            primaryNavigationFragment?.let { hide(it) }

            //Check if current fragment exists in fragmentManager
            if (current == null) {
                current = newFrag
                add(containerId, current!!, tag)
            } else {
                show(current!!)
            }
        }
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        .setPrimaryNavigationFragment(current)
        .setReorderingAllowed(true)
        .commitNowAllowingStateLoss()
}
