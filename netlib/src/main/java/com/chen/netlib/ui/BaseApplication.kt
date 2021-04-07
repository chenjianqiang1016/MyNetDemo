package com.chen.netlib.ui

import android.app.Application
import kotlin.properties.Delegates

open class BaseApplication : Application {

    constructor() : super()

    companion object {

        private var INSTANCE: BaseApplication by Delegates.notNull()

        fun getInstance(): BaseApplication {
            return INSTANCE
        }

    }


    override fun onCreate() {
        super.onCreate()

        INSTANCE = this
    }


}