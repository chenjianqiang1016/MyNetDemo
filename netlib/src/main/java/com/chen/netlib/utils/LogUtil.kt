package com.chen.netlib.utils

import android.util.Log

object LogUtil {

    private val LogTag: String = "MyNetDemo"

    /**
     * tag可以不传，如果需要单独指定，就需要传
     */
    fun e(msg: String, tag: String = "") {

        if (tag.isNotEmpty()) {
            Log.e(tag, msg)
        } else {
            Log.e(LogTag, msg)
        }
    }


}