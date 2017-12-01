package com.nilin.simpleweather

import android.content.Context
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.support.v4.widget.SwipeRefreshLayout
import android.util.AttributeSet


/**
 * Created by nilin on 2017/8/21.
 */
class CustomSwipeToRefresh(context: Context, attrs: AttributeSet) : SwipeRefreshLayout(context, attrs) {

    private val mTouchSlop: Int
    private var mPrevX: Float = 0.toFloat()

    init {
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {

        when (event.action) {
            MotionEvent.ACTION_DOWN -> mPrevX = MotionEvent.obtain(event).x
            MotionEvent.ACTION_MOVE -> {
                val eventX = event.x
                val xDiff = Math.abs(eventX - mPrevX)

                if (xDiff > mTouchSlop) {
                    return false
                }
            }
        }
        return super.onInterceptTouchEvent(event)
    }
}