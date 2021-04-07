package com.chen.netlib.utils

import android.content.Context

object DeviceUtil {

    fun getVersionName(context: Context): String {

        return try {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName
        } catch (e: Exception) {
            ""
        }

    }

}