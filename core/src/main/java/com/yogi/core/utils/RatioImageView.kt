package com.yogi.core.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.yogi.core.R

/**
 * Created by oohyugi on 2019-08-25.
 * github: https://github.com/oohyugi
 */
class RatioImageView : AppCompatImageView {

    private var mRatio = 1f

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        if (attrs != null) {
            @SuppressLint("CustomViewStyleable") val styled =
                context.obtainStyledAttributes(attrs, R.styleable.RatioView)
            mRatio = styled.getFloat(R.styleable.RatioView_ratio, 1f)
            styled.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = (width * mRatio).toInt()
        setMeasuredDimension(width, height)
        super.onMeasure(
            MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        )
    }
}
