package com.yogi.core.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by oohyugi on 2019-08-24.
 * github: https://github.com/oohyugi
 */
class MarginItemDecoration(private val space: Int,val type:Int=1, val spanScount:Int =2) : RecyclerView.ItemDecoration() {

    companion object{
        const val TYPE_VERTICAL  =1
        const val TYPE_GRID_VERTICAL  =3
        const val TYPE_HORIZONTAL  =2
        const val TYPE_GRID_HORIZONTAL  =4
    }
    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State) {
        with(outRect) {
            if (type == TYPE_VERTICAL){
                if (parent.getChildAdapterPosition(view) == 0) {
                    top = space
                }
                bottom = space
            }

            if (type == TYPE_HORIZONTAL){
                if (state.isPreLayout){
                    if (parent.childCount != 0) {
                        right = space
                    }

                }

//                if (parent.getChildAdapterPosition(view) == 0) {
//                    left = space
//                }
//
            }

            if (type == TYPE_GRID_VERTICAL){
                if (parent.getChildAdapterPosition(view) != 0) {
                    top = space
                }
            }
            if (type == TYPE_GRID_HORIZONTAL){
                val position = parent.getChildAdapterPosition(view) // item position
                val column = position % spanScount // item column
                outRect.left = column * space / spanScount // column * ((1f / spanCount) * spacing)
                outRect.right = space - (column + 1) * space / spanScount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanScount) {
                    outRect.top = space // item top
                }
            }


        }
    }
}