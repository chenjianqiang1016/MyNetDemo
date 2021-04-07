package com.chen.netlib.listener

import android.view.View

abstract class OnAvoidDoubleClickListener: View.OnClickListener {

    private val mThrottleFirstTime:Int=600

    private var mLastClickTime:Long=0L

    override fun onClick(v: View?) {

        var currentTime:Long= System.currentTimeMillis()

        if (currentTime - mLastClickTime <= 0) {//避免调整系统时间导致不可点击
            mLastClickTime = currentTime - mThrottleFirstTime
        }
        if (currentTime - mLastClickTime >= mThrottleFirstTime) {
            mLastClickTime = currentTime
            onNoDoubleClick(v)
        }


    }

    abstract fun onNoDoubleClick(v:View?)

}